<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/output">
        <input type="date" name="date" placeholder="дата листа">
        <button type="submit">Добавить</button>
    </form>
</@c.page>