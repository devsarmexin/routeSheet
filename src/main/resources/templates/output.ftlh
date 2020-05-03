<#import "parts/common.ftlh" as c>

<@c.page>
    Страница вывода, распечатки
<div>
    <table border="2">
        <tr>
            <td>Номер</td>
            <td>Дата</td>
            <td>Остаток нчало дня, л</td>
            <td>км, начало дня</td>
            <td>км, конец дня</td>
            <td>км</td>
            <td>заправка, л</td>
            <td>расход по норме, л</td>
            <td>расход фактический, л</td>
            <td>экономия</td>
            <td>остаток конец дня, л</td>
        </tr>
        <tr>
            <td>${routeSheet.number}</td>
            <td>${routeSheet.data}</td>
            <td>${routeSheet.getFuelStart()}</td>
            <td>${routeSheet.getMileageStart()}</td>
            <td>${routeSheet.getMileageFinish()}</td>
            <td><#if routeSheet.getDistance()??>${routeSheet.getDistance()}<else></else></#if></td>
            <td>${routeSheet.getFueling()}</td>
            <td>${routeSheet.getConsumptionNorm()}</td>
            <td>${routeSheet.getConsumptionFact()}</td>
            <td>${routeSheet.getSaving()}</td>
            <td>${routeSheet.getFuelFinish()}</td>
        </tr>
    </table>
    <br>
</div>
<div>
    <#list routeSheet.getAddress() as address>
        <table border="2">
            <tr>
                <td>Начальная точка</td>
                <td>Конечная точка</td>
                <td>Расстояние</td>
            </tr>
            <tr>
                <td>${address.getDeparture_point()}</td>
                <td>${address.getDestination()}</td>
                <td>${address.getDistance()}</td>
            </tr>
            <br>
        </table>
    </#list>
</div>
    <a href="/">Выход в меню</a>
</@c.page>