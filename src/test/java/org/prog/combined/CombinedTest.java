package org.prog.combined;

import com.mysql.cj.jdbc.Driver;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.prog.dto.PersonDto;
import org.prog.dto.ResponseDto;
import org.prog.web.GooglePage;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.List;

public class CombinedTest {

    private WebDriver driver;

    private GooglePage googlePage;

    private final static String INSERT_SQL =
            "INSERT INTO Persons (FirstName, LastName, Gender, Title, Nat) VALUES (?, ?, ?, ?, ?)";

    private final static String SELECT_RANDOM =
            "SELECT * FROM Persons ORDER BY RAND() LIMIT 1";

    @BeforeSuite
    public void setUp() {
        this.driver = new ChromeDriver();
        this.googlePage = new GooglePage(driver);
        driver.manage().window().maximize();
    }

    @AfterSuite
    public void tearDown() {
        Assert.assertNotNull(driver, "Driver has not been initialized!");
        driver.quit();
    }

    @Test
    public void combinedTest() throws SQLException {
        retrieveAndStoreUsers();
        googlePage.load();
        googlePage.acceptCookiesIfPresent();
        String userName = getRandomUserFromDB();
        googlePage.executeSearch(userName);
        List<WebElement> searchResults = googlePage.getSearchHeaders();

        int searchCounter = 0;
        for (WebElement searchHeader : searchResults) {
            if (searchHeader.getText().contains(userName)) {
                searchCounter++;
            }
        }
        Assert.assertTrue(searchCounter >= 3, "Less than 3 results for name " + userName);
    }

    public void retrieveAndStoreUsers() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            List<PersonDto> persons = getPersons(2);

            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db", "user", "password");

            statement = connection.createStatement();

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);

            for (PersonDto person : persons) {
                preparedStatement.setString(1, person.getName().getFirst());
                preparedStatement.setString(2, person.getName().getLast());
                preparedStatement.setString(3, person.getGender());
                preparedStatement.setString(4, person.getName().getTitle());
                preparedStatement.setString(5, person.getNat());

                try {
                    preparedStatement.execute();
                } catch (SQLException e) {
                    System.out.println("failed to store user " + person.getName().getFirst() + " " + person.getName().getLast());
                }
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public String getRandomUserFromDB() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db", "user", "password");

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_RANDOM);
            while (resultSet.next()) {
                return resultSet.getString("FirstName") + " " + resultSet.getString("LastName");
            }
            Assert.fail("No records found in DB");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        Assert.fail("DB Error!");
        return null;
    }

    private List<PersonDto> getPersons(int amount) {
        ResponseDto responseDto = RestAssured.given()
                .baseUri("https://randomuser.me/")
                .basePath("api/")
                .queryParam("inc", "gender,name,nat")
                .queryParam("noinfo")
                .queryParam("results", amount)
                .get()
                .as(ResponseDto.class);
        Assert.assertFalse(responseDto.getResults().isEmpty(),
                "At least one user must be retrieved!");
        return responseDto.getResults();
    }
}
