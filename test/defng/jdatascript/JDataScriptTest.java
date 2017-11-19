package defng.jdatascript;

import clojure.java.api.Clojure;
import clojure.lang.Atom;
import datascript.db.DB;
import defng.joculer.Joculer;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static defng.jdatascript.JDataScript.*;
import static defng.joculer.Joculer.listOf;
import static defng.joculer.Joculer.mapOf;
import static defng.joculer.Joculer.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JDataScriptTest {


    public static String diff(Object expected, Object actual) {
        return Joculer.diff("Expected", expected, "Actual", actual);
    }

    @Test
    public void test_dbWith() throws Exception {
        DB db = emptyDB();
        DB db1 =
                dbWith(db, listOf(listOf(":db/add", 1, "name", "Ivan"), listOf(":db/add", 1, "age", 17)));

        DB db2 = dbWith(db1, listOf(mapOf(":db/id", 2, "name", "Igor", "age", 35)));

        String query = "[:find ?n ?a :where [?e \"name\" ?n] [?e \"age\" ?a]]";

        {
            Object expected = setOf(listOf("Ivan", 17));
            Object actual = q(query, db1);
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
        {
            Object expected = setOf(listOf("Ivan", 17), listOf("Igor", 35));
            Object actual = q(query, db2);
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
    }

    @Test
    public void test_nestedMaps() throws Exception {
        String query = "[:find ?e ?a ?v :where [?e ?a ?v]]";
        DB db0 = emptyDB(mapOf("profile", mapOf(":db/valueType", ":db.type/ref")));
        {
            DB db = dbWith(db0, listOf(mapOf("name", "Igor", "profile", mapOf("email", "@2"))));
            Object actual = q(query, db);
            Object expected =
                    setOf(listOf(1, "name", "Igor"), listOf(1, "profile", 2), listOf(2, "email", "@2"));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }

        {
            DB db = dbWith(db0, listOf(mapOf("email", "@2", "_profile", mapOf("name", "Igor"))));
            Object actual = q(query, db);
            Object expected =
                    setOf(listOf(1, "email", "@2"), listOf(2, "name", "Igor"), listOf(2, "profile", 1));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }

        db0 = emptyDB(mapOf("user/profile", mapOf(":db/valueType", ":db.type/ref")));
        {
            DB db = dbWith(db0, listOf(mapOf("name", "Igor", "user/profile", mapOf("email", "@2"))));
            Object actual = q(query, db);
            Object expected =
                    setOf(listOf(1, "name", "Igor"), listOf(1, "user/profile", 2), listOf(2, "email", "@2"));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }

        {
            DB db = dbWith(db0, listOf(mapOf("email", "@2", "user/_profile", mapOf("name", "Igor"))));
            Object actual = q(query, db);
            Object expected =
                    setOf(listOf(1, "email", "@2"), listOf(2, "name", "Igor"), listOf(2, "user/profile", 1));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
    }

    @Test
    public void test_schema() {
        Map schema = mapOf("aka", mapOf(":db/cardinality", ":db.cardinality/many"));
        DB db =
                dbWith(
                        emptyDB(schema),
                        listOf(
                                listOf(":db/add", -1, "name", "Ivan"),
                                listOf(":db/add", -1, "aka", "X"),
                                listOf(":db/add", -1, "aka", "Y"),
                                mapOf(":db/id", -2, "name", "Igor", "aka", listOf("F", "G"))));
        String query = "[:find ?aka :in $ ?e :where [?e \"aka\" ?aka]]";

        {
            Object actual = q(query, db, 1);
            Object expected = Clojure.read("#{[\"X\"] [\"Y\"]}");
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
        {
            Object actual = q(query, db, 2);
            Object expected = Clojure.read("#{[\"F\"] [\"G\"]}");
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
    }

    @Test
    public void test_pull() {
        Map schema =
                mapOf(
                        "father", mapOf(":db/valueType", ":db.type/ref"),
                        "children",
                        mapOf(":db/valueType", ":db.type/ref", ":db/cardinality", ":db.cardinality/many"));
        DB db =
                dbWith(
                        emptyDB(schema),
                        listOf(
                                mapOf(":db/id", 1, "name", "Ivan", "children", listOf(10)),
                                mapOf(":db/id", 10, "father", 1, "children", listOf(100, 101)),
                                mapOf(":db/id", 100, "father", 10)));

        {
            Map actual = pull(db, "[\"children\"]", 1);
            Map expected = mapOf("children", listOf(mapOf(":db/id", 10)));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }

        {
            Map actual = pull(db, "[\"children\", {\"father\" [\"name\" :db/id]}]", 10);
            Map expected =
                    mapOf(
                            "children",
                            listOf(mapOf(":db/id", 100), mapOf(":db/id", 101)),
                            "father",
                            mapOf("name", "Ivan", ":db/id", 1));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
    }

    DB peopleDB =
            dbWith(
                    emptyDB(mapOf("age", mapOf(":db/index", true))),
                    listOf(
                            mapOf(":db/id", 1, "name", "Ivan", "age", 15),
                            mapOf(":db/id", 2, "name", "Petr", "age", 37),
                            mapOf(":db/id", 3, "name", "Ivan", "age", 37)));

    @Test
    public void test_q_coll() {
        {
            Set expected = setOf(listOf(1, "Ivan"), listOf(2, "Petr"), listOf(3, "Ivan"));
            Set actual =
                    (Set)
                            q(
                                    "[:find ?e ?name :in   $ [?name ...] :where [?e \"name\" ?name]]",
                                    peopleDB,
                                    listOf("Ivan", "Petr"));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }

        {
            Set expected = setOf(listOf(1), listOf(2));
            Set actual = (Set) q("[:find ?x  :in  [?x ...] :where [(pos? ?x)]]", listOf(-2, -1, 0, 1, 2));
            assertTrue(diff(expected, actual), Joculer.eq(expected, actual));
        }
    }

    @Test
    public void test_q_relation() {
        {
            Set res =
                    (Set)
                            q(
                                    "[:find ?e ?email :in    $ $b :where [?e \"name\" ?n] [$b ?n ?email]]",
                                    peopleDB,
                                    listOf(listOf("Ivan", "ivan@mail.ru"), listOf("Petr", "petr@gmail.com")));
            Object expected =
                    setOf(listOf(1, "ivan@mail.ru"), listOf(2, "petr@gmail.com"), listOf(3, "ivan@mail.ru"));
            assertTrue(diff(expected, res), Joculer.eq(expected, res));
        }

        {
            Set res =
                    (Set)
                            q(
                                    "[:find ?e ?email  :in    $ [[?n ?email]] :where [?e \"name\" ?n]]",
                                    peopleDB,
                                    listOf(listOf("Ivan", "ivan@mail.ru"), listOf("Petr", "petr@gmail.com")));
            Object expected =
                    setOf(listOf(1, "ivan@mail.ru"), listOf(2, "petr@gmail.com"), listOf(3, "ivan@mail.ru"));
            assertTrue(diff(expected, res), Joculer.eq(expected, res));
        }
    }

    @Test
    public void test_q_rules() {
        Set res =
                (Set)
                        q(
                                "[:find ?e1 ?e2  :in  $ %  :where (mate ?e1 ?e2) [(< ?e1 ?e2)]]",
                                peopleDB,
                                "[[(mate ?e1 ?e2)   [?e1 \"name\" ?n]  [?e2 \"name\" ?n]]  [(mate ?e1 ?e2)  [?e1 \"age\" ?a]  [?e2 \"age\" ?a]]]");
        Set expected = setOf(listOf(1, 3), listOf(2, 3));
        assertTrue(diff(expected, res), Joculer.eq(expected, res));
    }

    @Test
    public void test_find_specs() {
        {
            List res = (List) q("[:find [?name ...] :where [_ \"name\" ?name]]", peopleDB);
            List expected = listOf("Ivan", "Petr");
            assertTrue(diff(expected, res), Joculer.eq(expected, res));
        }

        {
            List res =
                    (List) q("[:find [?name ?age] :where [1 \"name\" ?name] [1 \"age\" ?age]]", peopleDB);
            List expected = listOf("Ivan", 15);
            assertTrue(diff(expected, res), Joculer.eq(expected, res));
        }
        {
            String res = (String) q("[:find ?name . :where [1 \"name\" ?name]]", peopleDB);
            assertEquals("Ivan", res);
        }
    }

    @Test
    public void test_upsert() {
        Map schema = mapOf(":my/tid", mapOf(":db/unique", ":db.unique/identity"));
        Atom conn = createConn(schema);

        transact(
                conn,
                listOf(
                        mapOf(
                                ":my/tid", "5x",
                                ":my/name", "Terin")));

        transact(
                conn,
                listOf(
                        mapOf(
                                ":my/tid", "5x",
                                ":my/name", "Charlie")));

        Set names =
                (Set) q("[:find ?name :where [?e \":my/tid\" \"5x\"] [?e \":my/name\" ?name]]", db(conn));
        List expected = listOf("Charlie");
        String testOutput = diff(expected, names);
        assertTrue(testOutput, Joculer.eq(expected, names));
    }

}