<#import "parts/common.ftlh" as c>

<@c.page>
    <div>Hello, ${hello}!</div>
    <div>Нет не одного заполненного путевого листа.</div>
    <div>Вам необходимо заполнить первый путевой лист.</div>
    <div>
        <form method="post" action="/primary_input">
            <input type="date" name="tripDate" required/>
            <label>Введите дату</label><br>
            <input type="number" name="waybillNumber" min="1" required placeholder="порядковый номер"/>
            <label>Введите номер путевого листа</label><br>
            <input type="number" name="fuelStart" value="fuelStart" required step="0.01" min="0" placeholder="0,00 литры"/>
            <label>Введите количество топлива на начало дня</label><br>
            <input type="number" name="fuelFinish" value="fuelFinish" required step="0.01" min="0" placeholder="0,00 литры"/>
            <label>Введите количество топлива в конце дня</label><br>
            <input type="number" name="mileageStart" value="mileageStart" required step="1" min="0" placeholder="0 километры"/>
            <label>Введите пробег на начало дня</label><br>
            <input type="number" name="mileageFinish" value="mileageFinish" required step="1" min="0" placeholder="0 километры"/>
            <label>Введите пробег на конец дня</label><br>
            <input type="number" name="fueling" value="fueling" required step="0.01" min="0" placeholder="0,00 литры"/>
            <label>Введите количество топлива, заправленного сегодня</label><br>
            <input type="number" name="consumptionNorm" value="consumptionNorm" required step="0.01" min="0" placeholder="0,00 литров на 100 километров"/>
            <label>Введите расход топлива по норме</label><br>
            <input type="number" name="consumptionFact" value="consumptionFact" required step="0.1" min="0" placeholder="0,0 литров на 100 километров"/>
            <label>Введите расход топлива фактический</label><br>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="reset">очистить</button>
            <button type="submit">Занести в базу данных</button>
        </form>
    </div>
</@c.page>