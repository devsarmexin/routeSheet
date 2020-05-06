<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Выберите дату маршрутного листа, который необходимо сформировать:</h5>
    <form method="get" action="/createWaybill">
        <input type="date" name="data" required placeholder="На какую дату ?">
        <button type="submit">ввести</button>
    </form>
</@c.page>