@GrabConfig(systemClassLoader=true)
@Grab(group='org.postgresql', module='postgresql', version='42.3.3')
import groovy.sql.Sql

def dbUrl      = "jdbc:postgresql://localhost:5432/camoHealth"
def dbUser     = "postgres"
def dbPassword = " "
def dbDriver   = "org.postgresql.Driver"

def tableName  = "users"

def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

String selectStatement = """ 
    SELECT zipcode FROM ${tableName}
"""
String updateStatement = """ 
    update ${tableName} set user_type=2 where zipcode=?
"""
def rowNum = 0
sql.query(selectStatement) { resultSet ->
    while (resultSet.next()) {
        def code = resultSet.getInt(1)
        if(code<999999){
            sql.executeUpdate(updateStatement,code);
        }

        println(String.format("Code: %d\n",code));
        rowNum++
    }
    println(rowNum + " row(s) affected")
}