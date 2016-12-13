/**
 * Copyright (C) 2016 Joerg Bellmann (joerg.bellmann@googlemail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.siggici.hookserver.accounts.jdbc;

public class JdbcStatements {

    public static final String DEF_CREATE_USER_SQL = "insert into hookusers (username, password, enabled) values (?,?,?)";
    public static final String DEF_DELETE_USER_SQL = "delete from hookusers where username = ?";
    public static final String DEF_UPDATE_USER_SQL = "update hookusers set password = ?, enabled = ? where username = ?";
    public static final String DEF_INSERT_AUTHORITY_SQL = "insert into hookauthorities (username, authority) values (?,?)";
    public static final String DEF_DELETE_USER_AUTHORITIES_SQL = "delete from hookauthorities where username = ?";
    public static final String DEF_USER_EXISTS_SQL = "select username from hookusers where username = ?";
    public static final String DEF_CHANGE_PASSWORD_SQL = "update hookusers set password = ? where username = ?";

    public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled " + "from hookusers "
            + "where username = ?";

    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select username,authority "
            + "from hookauthorities " + "where username = ?";

}
