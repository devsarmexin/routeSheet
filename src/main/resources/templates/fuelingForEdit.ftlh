<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/createNewRouteSheet">
        <input type="date" name="tripDate" required/> Дата<br><br>
        <input type="number" name="fueling" required step="1" min="0" placeholder="0 литры"> Введите объём заправки<br><br>
        <h5>ПОДТВЕРДИТЕ РЕДАКТИРОВАНИЕ</h5><br>
        <input type="radio" name="isEdit" value="yes">ДА<br>
        <input type="radio" name="isEdit" value="no">НЕТ<br>
        <button type="submit">Ввести</button>
    </form>
</@c.page>