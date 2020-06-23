<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Вы точно хотите удалить Базу Данных?</h5>
    <form method="get" action="/delete">
        <input type="radio" name="isDelete" value="yes">Да<br>
        <input type="radio" name="isDelete" value="no">Нет<br>
        <button type="submit">Далее</button>
    </form>
</@c.page>