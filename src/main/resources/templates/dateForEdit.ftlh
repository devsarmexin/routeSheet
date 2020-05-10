<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Выберите дату маршрутного листа, который редактируется:</h5>
    <form method="get" action="/waybillEditing">
        <input type="date" name="date" required placeholder="На какую дату ?">
        <button type="submit">ввести</button>
    </form>
</@c.page>