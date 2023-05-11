package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String matchesPlayedPerYear = "SELECT season,COUNT(season) " +
                " FROM matches " +
                " GROUP BY season " +
                " ORDER BY season;";
        printResultForQuery(matchesPlayedPerYear);

        String matchesWonPerTeam = "SELECT winner,COUNT(winner) AS wins " +
                "FROM  matches " +
                "WHERE winner != '' " +
                "GROUP BY winner " +
                "ORDER BY wins DESC;";
        printResultForQuery(matchesWonPerTeam);

        String concededRunsPerTeamFor2016 = "SELECT d.bowling_team AS team,SUM(d.extra_runs) AS conceded_runs " +
                "FROM matches AS m " +
                "JOIN deliveries AS d " +
                "ON m.id=d.match_id " +
                "WHERE m.season=2016 " +
                "GROUP BY d.bowling_team " +
                "ORDER BY conceded_runs DESC;";
        printResultForQuery(concededRunsPerTeamFor2016);

        String topEconomicBowlersIn2015 = "SELECT d.bowler, " +
                "SUM(d.wide_runs+d.noball_runs+d.batsman_runs)/(SUM(if(d.wide_runs=0 AND d.noball_runs=0,1,0))/6) AS economy_rate " +
                "FROM matches AS m " +
                "JOIN deliveries AS d " +
                "ON m.id=d.match_id " +
                "WHERE season=2015 " +
                "GROUP BY d.bowler " +
                "ORDER BY economy_rate " +
                "LIMIT 10;";
        printResultForQuery(topEconomicBowlersIn2015);
    }

    private static void printResultForQuery(String query) {
        String driver = "jdbc:mysql://localhost:3306/Local_DB";
        String userName = "Surendra";
        String password = "password";
        try {
            Connection connection = DriverManager.getConnection(driver, userName, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                for (int row = 1; row <= resultSetMetaData.getColumnCount(); row++)
                    System.out.print(resultSet.getString(row) + "     ");

                System.out.println();
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}