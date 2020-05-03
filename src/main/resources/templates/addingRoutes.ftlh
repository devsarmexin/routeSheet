<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/${path}">
        <input type="number" name="distance" required placeholder="Расстояние"><br>
        <input type="text" name="address1" required placeholder="Откуда"/><br>
        <input type="text" name="address2" required placeholder="Куда"/><br>
        <h5>Есть ещё адреса?</h5><br>
        <input type="radio" name="flag" value="yes">есть<br>
        <input type="radio" name="flag" value="no">нет<br>
        <button type="reset">очистить</button>
        <button type="submit">далее</button>
        <br>
    </form>
</@c.page>