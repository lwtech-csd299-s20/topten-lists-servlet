<!DOCTYPE html>
<html>
    <head>
        <title>Top Top 10 List - ${topTenList.description}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Top Top 10 List #${listNumber}</h1>
        <h2>${topTenList.description}</h2>

        <ol start=10 reversed>
        <#list topTenList.items as item>
            <li>${item}</li>
        </#list>
        </ol>

        <a href="?cmd=show&index=${prevIndex}">Previous</a> &nbsp; &nbsp;
        <a href="?cmd=show&index=${nextIndex}">Next</a><br/>
    </body>
</html>
