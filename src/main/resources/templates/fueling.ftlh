<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/fueling">
        <input type="date" name="data" required/><br>
        Введите объём заправки
        <input type="number" name="fuel" required placeholder="литры">
        <button type="submit">вести</button>
    </form>
</@c.page>