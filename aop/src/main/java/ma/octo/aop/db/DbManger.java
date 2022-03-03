package ma.octo.aop.db;

import ma.octo.aop.entity.Language;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DbManger implements InitializingBean, DisposableBean {

    @Value("${db.url}")
    public  String dbUrl;
    @Value("${db.username}")
    public  String dbUsername;
    @Value("${db.password}")
    public  String dbPassword;
    private Connection connection;

    private JdbcDataSource setDatabaseProperties() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser(dbUsername);
        dataSource.setPassword(dbPassword);
        return  dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JdbcDataSource dataSource = setDatabaseProperties();

        try {
            connection = dataSource.getConnection();
            setData(connection);
        }
        catch (SQLException exception){
            System.out.println("ERROR");
        }
    }

    private void setData(Connection connection) throws SQLException {
        var statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS language;");
        statement.execute("CREATE TABLE language(id VARCHAR(255), name VARCHAR(255), author VARCHAR(255), fileExtension VARCHAR(255));");
        statement.execute("INSERT INTO language(id, name, author, fileExtension)\n" +
                "       VALUES('java', 'Java', 'James Gosling', 'java'),\n" +
                "            ('cpp', 'C++', 'Bjarne Stroustrup', 'cpp'),\n" +
                "            ('csharp', 'C#', 'Andres Hejlsberg', 'cs'),\n" +
                "            ('perl', 'Perl', 'Larry Wall', 'pl'),\n" +
                "            ('haskel', 'Haskell', 'Simon Peyton', 'hs'),\n" +
                "            ('lua', 'Lua', 'Luiz Henrique', 'lua'),\n" +
                "            ('python', 'Python', 'Guido van Rossum', 'py');");
    }

    public Optional<Language> getLanguageById(String id) {

        PreparedStatement preStm = null;
        Language language = null;

        try {
            preStm = connection.prepareStatement("select * from language where id = ?");
            preStm.setString(1, id);
            var resultSet = preStm.executeQuery();
            if (resultSet.next()){
                language = new Language(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("fileExtension")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preStm != null){
                try {
                    preStm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return language != null ? Optional.of(language) : Optional.empty();
    }

    public Optional<Language> getLanguageByExtension(String extension) {

        PreparedStatement preStm = null;
        Language language = null;

        try {
            preStm = connection.prepareStatement("select * from language where fileExtension = ?");
            preStm.setString(1, extension);
            var resultSet = preStm.executeQuery();
            if (resultSet.next()){
                language = new Language(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("fileExtension")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preStm != null){
                try {
                    preStm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return language != null ? Optional.of(language) : Optional.empty();
    }

    public List<Language> findAllLanguages() {
        List<Language> languages = new ArrayList<>();

        PreparedStatement preStm = null;

        try {
            preStm = connection.prepareStatement("select * from language");
            var resultSet = preStm.executeQuery();
            while (resultSet.next()){
                languages.add( new Language(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("fileExtension")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preStm != null){
                try {
                    preStm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return languages;
    }

    @Override
    public void destroy() throws Exception {
        if (connection != null){
            connection.close();
        }
    }
}
