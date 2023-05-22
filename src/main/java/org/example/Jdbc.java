package org.example;

import java.sql.*;

public class Jdbc {

    private static final String driver = "jdbc:mysql://localhost:3306/Local_DB";
    private static final String userName = "Surendra";
    private static final String password = "password";
    private static Connection connection = null;

    public static void main(String[] args) {

        try {
            connection = DriverManager.getConnection(driver, userName, password);
            Jdbc jdbc = new Jdbc();

            jdbc.totalMatchesPlayedPerYear();
            jdbc.totalMatchesWonPerTeam();
            jdbc.totalConcededRunsPerTeamFor2016();
            jdbc.topEconomicBowlersForYear2015();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void totalMatchesPlayedPerYear() throws SQLException {
        String matchesPlayedPerYear = "SELECT season,COUNT(season) AS totalMatches" +
                " FROM matches " +
                " GROUP BY season " +
                " ORDER BY season;";

        PreparedStatement preparedStatement = connection.prepareStatement(matchesPlayedPerYear);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("season") + " " + resultSet.getString("totalMatches"));
        }
        System.out.println();
        preparedStatement.close();
        resultSet.close();
    }

    private void totalMatchesWonPerTeam() throws SQLException {
        String matchesWonPerTeam = "SELECT winner,COUNT(winner) AS wins " +
                "FROM  matches " +
                "WHERE winner != '' " +
                "GROUP BY winner " +
                "ORDER BY wins DESC;";

        PreparedStatement preparedStatement = connection.prepareStatement(matchesWonPerTeam);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("winner") + " " + resultSet.getString("wins"));
        }
        System.out.println();
        preparedStatement.close();
        resultSet.close();
    }

    private void totalConcededRunsPerTeamFor2016() throws SQLException {
        String concededRunsPerTeamFor2016 = "SELECT d.bowling_team AS team,SUM(d.extra_runs) AS conceded_runs " +
                "FROM matches AS m " +
                "JOIN deliveries AS d " +
                "ON m.id=d.match_id " +
                "WHERE m.season=2016 " +
                "GROUP BY d.bowling_team " +
                "ORDER BY conceded_runs DESC;";

        PreparedStatement preparedStatement = connection.prepareStatement(concededRunsPerTeamFor2016);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("team") + " " + resultSet.getString("conceded_runs"));
        }
        System.out.println();
        preparedStatement.close();
        resultSet.close();
    }

    private void topEconomicBowlersForYear2015() throws SQLException {
        String topEconomicBowlersIn2015 = "SELECT d.bowler AS bowler, " +
                "SUM(d.wide_runs+d.noball_runs+d.batsman_runs)/(SUM(if(d.wide_runs=0 AND d.noball_runs=0,1,0))/6) AS economy_rate " +
                "FROM matches AS m " +
                "JOIN deliveries AS d " +
                "ON m.id=d.match_id " +
                "WHERE season=2015 " +
                "GROUP BY d.bowler " +
                "ORDER BY economy_rate " +
                "LIMIT 10;";

        PreparedStatement preparedStatement = connection.prepareStatement(topEconomicBowlersIn2015);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("bowler") + " " + resultSet.getString("economy_rate"));
        }
        System.out.println();
        preparedStatement.close();
        resultSet.close();
    }
}