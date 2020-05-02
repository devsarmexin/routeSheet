<#import "parts/common.ftlh" as c>

<@c.page>
    <div>Hello, Natalia!</div>
    <div>Нет не одного заполненного путевого листа.</div>
    <div>Вам необходимо заполнить первый путевой лист.</div>
    <div>
        <form method="post" action="/primary_input">
            <input type="date" name="dateToString" required/>
            <label>Введите дату</label><br>
            <input type="number" name="number" required placeholder="порядковый номер"/>
            <label>Введите номер путевого листа</label><br>
            <input type="number" name="fuelStart" value="fuelStart" required placeholder="литры"/>
            <label>Введите количество топлива на начало дня</label><br>
            <input type="number" name="fuelFinish" value="fuelFinish" required placeholder="литры"/>
            <label>Введите количество топлива в конце дня</label><br>
            <input type="number" name="mileageStart" value="mileageStart" required placeholder="километры"/>
            <label>Введите пробег на начало дня</label><br>
            <input type="number" name="mileageFinish" value="mileageFinish" required placeholder="километры"/>
            <label>Введите пробег на конец дня</label><br>
            <input type="number" name="fueling" value="fueling" required placeholder="литры"/>
            <label>Введите количество топлива, заправленного сегодня</label><br>
            <input type="number" name="consumptionNorm" value="consumptionNorm" required placeholder="литров на 100 километров"/>
            <label>Введите расход топлива по норме</label><br>
            <input type="number" name="consumptionFact" value="consumptionFact" required placeholder="литров на 100 километров"/>
            <label>Введите расход топлива фактический</label><br>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Занести в базу данных</button>
        </form>
    </div>
   <a href="/">Переход в меню</a>
</@c.page>