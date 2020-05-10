<#import "parts/common.ftlh" as c>

<@c.page>
    <p>Последний путевой лист за ${lastNumber} число</p>
    Всего путевых листов:
    <div>
        <#list routeSheetList as routeSheet>
            <td>${routeSheet.getData()}</td><br>
        </#list>
    </div>

</@c.page>