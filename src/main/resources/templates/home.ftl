<!DOCTYPE html>
<html>
    <head>
        <title>TopTopTenLists.com - Homepage</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Top Top 10 Lists</h1>
        <h2>The Best TopTen Lists on the Internet!</h2>

        <a href="?cmd=show">Show me the lists!</a> &nbsp; &nbsp;

        <#if loggedIn>
            <a href="?cmd=add">Add a New List</a><br />
            <a href="?cmd=logout">Log Out</a>
        <#else>
            <a href="?cmd=login">Log In</a><br />
            <a href="?cmd=register">Register</a>
        </#if>
    </body>
</html>
