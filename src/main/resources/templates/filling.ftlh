<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <form method="post" action="add">
<#--            <p>Последний запоненный день : ${lastRouteSheet.data}</p>-->
            <input type="date" name="dateToString" required/><br>
            <input type="number" name="fueling" required placeholder="Заправка, л"/><br>
            <input type="text" name="address1" required placeholder="Откуда"/><br>
            <input type="text" name="address2" required placeholder="Куда"/><br>
            <input type="text" name="distance" required placeholder="Расстояние"/><br>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/><br>
            <input type="radio" name="flag" required value="yes">Есть ещё адреса<br>
            <input type="radio" name="flag" required value="no">нет адресов<br>
            <button type="submit">Добавить</button>
            <br>
        </form>
    </div>
    
</@c.page>