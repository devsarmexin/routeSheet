<#import "parts/common.ftlh" as c>

<@c.page>
    Маршрутный лист на : ${numberRouteSheet}
    <br><br>
<div>
    <table border="2">
        <tr>
            <td>Номер</td>
            <td>Дата</td>
            <td>Остаток на начало дня, л</td>
            <td>км, начало дня</td>
            <td>км, конец дня</td>
            <td>км</td>
            <td>заправка, л</td>
            <td>расход по норме, л</td>
            <td>расход фактический, л</td>
            <td>экономия</td>
            <td>остаток на конец дня, л</td>
        </tr>
        <tr>
            <td>${routeSheet.waybillNumber}</td>
            <td>${routeSheet.tripDate}</td>
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
    <#list routeSheet.getRoutes() as routes>
        <table border="2">
            <tr>
                <td>Начальная точка</td>
                <td>Конечная точка</td>
                <td>Расстояние</td>
            </tr>
            <tr>
                <td>${routes.getDeparture_point()}</td>
                <td>${routes.getDestination()}</td>
                <td>${routes.getDistance()}</td>
            </tr>
            <br>
        </table>
    </#list>
</div>
</@c.page>