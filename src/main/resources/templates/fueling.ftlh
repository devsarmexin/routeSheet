<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/createNewRouteSheet">
        <input type="date" name="tripDate" required/> Дата<br><br>
        <input type="number" name="fueling" required step="1" min="0" placeholder="0 литры"> Введите объём заправки<br><br>
        <button type="reset">очистить</button>
        <button type="submit">Ввести</button>
    </form>
</@c.page>