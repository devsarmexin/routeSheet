<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Выберите дату путевого листа, для которого буду созданы маршруты:</h5>
    <form method="get" action="/editRoute">
        <input type="date" name="date" required placeholder="На какую дату ?">
        <button type="submit">ввести</button>
    </form>
</@c.page>