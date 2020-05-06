<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/fueling">
        <input type="date" name="data" required/>Введите дату<br>
        <input type="number" name="fuel" required placeholder="литры">Введите объём заправки<br>
        <button type="submit">Ввести</button>
    </form>
</@c.page>