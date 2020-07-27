<#import "parts/common.ftlh" as c>

<@c.page>
    Всего путевых листов:
    <div>
        <#list routeSheetList as routeSheet>
            <td>${routeSheet.getTripDate()}</td><br>
        </#list>
    </div>

</@c.page>