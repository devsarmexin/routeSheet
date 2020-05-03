<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <form method="post" action="add">
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
    <a href="/">Выход в меню</a>
</@c.page>