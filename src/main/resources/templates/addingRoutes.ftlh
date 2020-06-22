<#import "parts/common.ftlh" as c>

<@c.page>
    Первая точка: ${firstPoint}
    <form method="get" action="/editingRoutes">
        <input type="number" name="distance" required step="1" min="0" placeholder="Расстояние"><br>
        <input type="text" name="routeEndPointAddress" required placeholder="Куда"/><br>
        <h5>Есть ещё адреса?</h5><br>
        <input type="radio" name="isThereFollowingRoute" value="yes">есть<br>
        <input type="radio" name="isThereFollowingRoute" value="no">нет<br>
        <button type="reset">очистить</button>
        <button type="submit">далее</button>
        <br>
    </form>
</@c.page>