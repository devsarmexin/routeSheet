<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Выберите дату маршрутного листа, для которого добавляются маршруты:</h5>
    <form method="get" action="/output">
        <input type="date" name="date" required placeholder="На какую дату ?">
        <button type="submit">ввести</button>
    </form>
</@c.page>