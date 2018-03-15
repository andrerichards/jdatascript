package defng.jdatascript;


import clojure.java.api.Clojure;
import clojure.lang.Atom;
import clojure.lang.IFn;
import datascript.db.DB;
import datascript.db.TxReport;
import defng.joculer.Joculer;

import java.util.List;
import java.util.Map;

import static defng.joculer.Joculer.listOf;

/**
 * Java wrapper functions for various DataScript functions
 */
public class JDataScript {

    static final String NAMESPACE = "defng.jdatascript.core";

    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("datascript.core"));
        require.invoke(Clojure.read(NAMESPACE));
    }

    static IFn GET = Clojure.var("clojure.core", "get");

    static IFn EMPTY_DB = Clojure.var("datascript.core", "empty-db");
    static IFn SCHEMA_TO_CLJ = Clojure.var(NAMESPACE, "schema->clj");
    static IFn CREATE_CONN = Clojure.var("datascript.core", "create-conn");
    static IFn CONN_FROM_DB = Clojure.var("datascript.core", "conn-from-db");
    static IFn DB = Clojure.var("datascript.core", "db");
    static IFn DB_WITH = Clojure.var(NAMESPACE, "db-with");
    static IFn TRANSACT = Clojure.var(NAMESPACE, "transact");
    static IFn Q = Clojure.var("datascript.core", "q");
    static IFn PULL = Clojure.var("datascript.core", "pull");
    static IFn PULL_MANY = Clojure.var("datascript.core", "pullMany");
    static IFn PULL_RESULT_TO_JAVA = Clojure.var(NAMESPACE, "pull-result->java");


    public static DB emptyDB() {
        return (DB) EMPTY_DB.invoke();
    }

    public static DB emptyDB(Map schema) {
        return (DB) EMPTY_DB.invoke(SCHEMA_TO_CLJ.invoke(schema));
    }
    
    public static Atom createConn() {
        return (Atom) CREATE_CONN.invoke();
    }

    public static Atom createConn(Map schema) {
        return (Atom) CREATE_CONN.invoke(schema);
    }

    public static Atom connFromDB(DB db) {
        return (Atom) CONN_FROM_DB.invoke(db);
    }

    public static DB db(Atom conn) {
        return (DB) DB.invoke(conn);
    }

    public static DB dbWith(DB db, List txData) {
        return (DB) DB_WITH.invoke(db, txData);
    }

    public static TxReport transact(Atom conn, Map txData) {
        return (TxReport) TRANSACT.invoke(conn, Joculer.toClojure(listOf(txData)));
    }

    public static TxReport transact(Atom conn, List txData) {
        return (TxReport) TRANSACT.invoke(conn, Joculer.toClojure(txData));
    }

    public static Object q(Object query, java.lang.Object... inputs) {
        Object queryVectorOrMap = query instanceof String
                ? Clojure.read((String) query)
                : query;
        if (inputs != null) {
            switch (inputs.length) {
                case 0:
                    return Q.invoke(queryVectorOrMap);
                case 1:
                    return Q.invoke(queryVectorOrMap, inputs[0]);
                case 2:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1]);
                case 3:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2]);
                case 4:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3]);
                case 5:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4]);
                case 6:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5]);
                case 7:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6]);
                case 8:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7]);
                case 9:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8]);
                case 10:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9]);
                case 11:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10]);
                case 12:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11]);
                case 13:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12]);
                case 14:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13]);
                case 15:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14]);
                case 16:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14], inputs[15]);
                case 17:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14], inputs[15], inputs[16]);
                case 18:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14], inputs[15], inputs[16], inputs[17]);
                case 19:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14], inputs[15], inputs[16], inputs[17], inputs[18]);
                case 20:
                    return Q.invoke(queryVectorOrMap, inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11], inputs[12], inputs[13], inputs[14], inputs[15], inputs[16], inputs[17], inputs[18], inputs[19]);
            }
        }
        return Q.invoke(queryVectorOrMap);
    }

    public static Map pull(DB db, String pattern, Object entityId) {
        return (Map) PULL_RESULT_TO_JAVA.invoke(PULL.invoke(db, Clojure.read(pattern), entityId));
    }

    public static List<Map> pullMany(DB db, String pattern, List entityIds) {
        return (List) PULL_RESULT_TO_JAVA.invoke(PULL_MANY.invoke(db, Clojure.read(pattern), entityIds));
    }

    public enum Operation {
        ADD(":db/add"),
        RETRACT(":db/retract");

        private String value;

        Operation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }
    }

    public static List tx(Object entityID, String key, Object value) {
        return listOf(Operation.ADD, entityID, key, value);
    }

    public static List tx(Operation operation, Object entityID, String key, Object value) {
        return listOf(operation.value, entityID, key, value);
    }

}
