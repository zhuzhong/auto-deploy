package com.z.deploy;

import java.io.IOException;

import ch.ethz.ssh2.Connection;

public class RmtConnection {

    public Connection getConnection(String host, String user, String pass) throws IOException {
        Connection con = new Connection(host);
        con.connect();
        boolean isAuthed = con.authenticateWithPassword(user, pass);
        System.out.println("isAuthed====" + isAuthed);
        return con;
    }
}
