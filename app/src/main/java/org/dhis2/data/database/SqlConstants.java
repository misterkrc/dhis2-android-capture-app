package org.dhis2.data.database;

public class SqlConstants {
    public static final String SELECT = "SELECT ";
    public static final String SELECT_DISTINCT = "SELECT DISTINCT ";
    public static final String FROM = " FROM ";
    public static final String WHERE = " WHERE ";
    public static final String WHEN = " WHEN ";
    public static final String THEN = " THEN ";
    public static final String ELSE = " ELSE ";
    public static final String END = " END ";
    public static final String LIMIT_1 = " LIMIT 1";
    public static final String LIMIT_10 = " LIMIT 10";
    public static final String DESC = " DESC ";
    public static final String ASC = " ASC ";
    public static final String JOIN = " JOIN ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String QUOTE = "'";
    public static final String ON = " ON ";
    public static final String IN = " IN ";
    public static final String POINT = ".";
    public static final String EQUAL = " = ";
    public static final String NOT_EQUAL = " != ";
    public static final String IS_NOT_NULL = " IS NOT NULL ";
    public static final String IS_NULL = " IS NULL ";
    public static final String LESS_OR_EQUAL = " <= ";
    public static final String LESS_THAN = " < ";
    public static final String GREAT_THAN = " > ";
    public static final String ALL = "*";
    public static final String QUESTION_MARK = "?";
    public static final String COMMA = ", ";
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String TABLE_POINT_FIELD_EQUALS = "%s.%s = ";
    public static final String TABLE_POINT_FIELD_NOT_EQUALS = "%s.%s != ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String ORDER_BY_CASE = " ORDER BY CASE ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String AS = " AS ";
    public static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    public static final String VARIABLE = "%s";
    public static final String TABLE_POINT_FIELD = "%s.%s";
    public static final String JOIN_VARIABLE_ON_TABLE_POINT_FIELD_EQUALS = " JOIN %s ON %s.%s = %s.%s";

    private SqlConstants() {
        // hide public constructor
    }
}
