/**
 * Created by user on 2015/12/3.
 */
function ccs(obj)
{
    var os = obj.parentNode.parentNode;

    var m = os.cells[2].childNodes[0].value;

    if (m == '')
    {
        m = 0;
    }

    var p = os.cells[3].childNodes[0].value;

    os.cells[4].childNodes[0].value = mul(m, p);

    var tem = os.cells[4].childNodes[0].value;

    if (tem == 'NaN')
    {
        os.cells[4].childNodes[0].value = '';
        return;
    }

    tem = tem + "";

    os.cells[4].childNodes[0].value = formatNum(tem, 2);

    //total();

}

function cc(obj)
{
    ccs(obj);
    //total();
}

function blu(obj)
{
    blus(obj);
    ccs(obj);
}



function blus(obj)
{
    obj.value = trim(obj.value);
    var tem = obj.value;

    if (tem.indexOf('.') == -1)
    {
        if (tem == '')
        {
            obj.value = '0.00';
            return;
        }
        obj.value = tem + '.00';
        return;
    }

    tem += '00';
    obj.value = tem.substring(0, tem.indexOf('.') + 3);
}

function addTr()
{
    var length = document.getElementsByName("productName").length;

    if (length > 15)
    {
        alert('�?多只能有15个产�?');
        return null;
    }

    var table = $O("tables");
    var tr = $O("trCopy");

    trow = 	table.insertRow(-1);

    if (length % 2 == 1)
    {
        trow.className = 'content2';
    }
    else
    {
        trow.className = 'content1';
    }

    for (var i = 0; i < tr.cells.length - 1; i++)
    {
        var tcell = document.createElement("td");

        tcell.innerHTML = tr.cells[i].innerHTML;

        trow.appendChild(tcell);
    }

    tcell = document.createElement("td");

    tcell.innerHTML = '<input type=button value="删除" class=button_class onclick="removeTr(this)">';

    trow.appendChild(tcell);

    //total();

    return trow;
}

function removeTr(obj)
{
    //obj.parentNode.parentNode.removeNode(true);
    //2015/12/4 removeNode() does not work for Chrome
    obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);

    //rows
    var table = $O("tables");

    for (var i = 2; i < table.rows.length; i++)
    {
        if (i % 2 == 1)
        {
            table.rows[i].className = 'content1';
        }
        else
        {
            table.rows[i].className = 'content2';
        }
    }
}

function toUnqueStr1(ele)
{
    return ele.ppid + '-' + ele.pprice + '-' + ele.pstafferid + '-' + ele.pdepotpartid;
}

function toUnqueStr2(ele)
{
    return ele.productid + '-' + ele.price + '-' + ele.stafferid + '-' + ele.depotpartid;
}

function distinc(ox)
{
    //productid
    var plist = document.getElementsByName('productName');

    var arr1 = new Array();

    for(var i = 0; i < plist.length; i++)
    {
        arr1[i] = toUnqueStr2(plist[i]);
    }

    var arr = new Array();
    var n = 0;
    for(var k = 0; k < ox.length; k++)
    {
        var ff = false;
        for(var i = 0; i < arr1.length; i++)
        {
            if (arr1[i] == toUnqueStr1(ox[k]))
            {
                ff = true;
            }
        }

        if (!ff)
        {
            arr1.push(toUnqueStr1(ox[k]));

            arr[n++] = ox[k];
        }
    }

    return arr;
}

function clears()
{
    document.getElementById('unAmount').value = '';
    document.getElementById('unAmount').title = '';
    document.getElementById('unAmount').oncheck = '';
    document.getElementById('unPrice').value = '';
    document.getElementById('unProductName').value = '';
    document.getElementById('unProductName').productid = '';
    document.getElementById('unProductName').productcode = '';
    document.getElementById('unDesciprt').value = '';
    document.getElementById('unRstafferName').value = '';
}

function clearsAll()
{
    clearArray(document.getElementsByName('productName'));
    clearArray(document.getElementsByName('amount'), true);
    clearArray(document.getElementsByName('price'));
    clearArray(document.getElementsByName('value'));
    clearArray(document.getElementsByName('desciprt'));
    clearArray(document.getElementsByName('rstafferName'));
}

function clearArray(array, flag)
{
    for (var i = 0; i < array.length; i++)
    {
        array[i].value = '';

        if (flag)
            array[i].oncheck = '';

        if (array[i].productid)
        {
            array[i].productid = '';
        }

        if (array[i].productcode)
        {
            array[i].productcode = '';
        }
    }
}

/**
 * oo����addStockArrival.jsp���涨���?
 */
function getProductRelation(ox)
{
    ox = distinc(ox);

    if (ox.length <= 0)
    {
        return;
    }

    var indes = 0;

    if (indes == 0)
    {
        indes = 1;

        setObj(oo, ox[0]);

        var os = oo.parentNode.parentNode;
        os.cells[2].childNodes[0].title = '��ǰ��Ʒ���������?:' + ox[0].pamount;
        os.cells[2].childNodes[0].oncheck = 'range(-' + ox[0].pamount + ')';
        os.cells[3].childNodes[0].value = ox[0].pprice;
        os.cells[5].childNodes[0].value = ox[0].pprice;
        os.cells[6].childNodes[0].value =  ox[0].pdepotpartname + '-->' + ox[0].pstaffername;
    }

    for(var i = indes; i < ox.length; i++)
    {
        var newTr = addTr();

        if (newTr == null)
        {
            break;
        }

        var inps = newTr.getElementsByTagName('INPUT');

        setObj(inps[0], ox[i]);

        inps[1].title = '��ǰ��Ʒ���������?:' + ox[i].pamount;
        inps[1].oncheck = 'range(-' + ox[i].pamount + ')';
        inps[2].value = ox[i].pprice;
        inps[4].value = ox[i].pprice;
        inps[5].value = ox[i].pdepotpartname + '-->' + ox[i].pstaffername;
    }
}

/**
 * �����Ʒ��ѡ��?
 */
function getProductAbs(ox)
{
    if (ox.length <= 0)
    {
        return;
    }

    var indes = 0;

    if (indes == 0)
    {
        indes = 1;

        setObj2(oo, ox[0]);
    }

    for(var i = indes; i < ox.length; i++)
    {
        var newTr = addTr();

        if (newTr == null)
        {
            break;
        }

        var inps = newTr.getElementsByTagName('INPUT');

        setObj2(inps[0], ox[i]);
    }
}

function getProduct(oos)
{
    var obj = oos[0];

    oo.value = obj.pname;
    oo.productid = obj.value;
}

function setObj(src, dest)
{
    src.value = dest.pname;
    src.productcode = dest.pcode;

    src.productid = dest.value;
    src.price = dest.pprice;
    src.stafferid = dest.pstafferid;
    src.depotpartid = dest.pdepotpartid;
}

function setObj2(src, dest)
{
    src.value = dest.name;
    src.productcode = dest.code;

    src.productid = dest.id;
}

function showDate(){
    $('.datepicker').pickadate({
        format:'yyyy-mm-dd',
        monthsFull:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
        monthsShort:["一","二","三","四","五","六","七","八","九","十","十一","十二"],
        weekdaysFull:["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
        weekdaysShort:["日","一","二","三","四","五","六"],
        today:"今日",clear:"清除",close:"关闭",firstDay:1,formatSubmit:"yyyy-mm-dd"
    });
}

function addRow(){
    addTr();
    showDate();
}

function lverify()
{
    var productIds = document.getElementsByName('productId');
    var amounts = document.getElementsByName('amount');
    var productMap2 = {}
    for (var i=0;i<productIds.length; i++){
        var productIdNode = productIds[i];
        var amountNode = amounts[i];
        var productId = productIdNode.value;
        var amount = amountNode.value;
//                console.log(productIdNode);
//                console.log(amountNode);
//                console.log(productId);
//                console.log(amount);
        if (amount>0){
            if (productId in productMap2){
                productMap2[productId] = parseInt(productMap2[productId])+parseInt(amount)
            } else{
                productMap2[productId] = parseInt(amount)
            }
        }
    }
//            console.log(productMap2);
    //object equal
    if (JSON.stringify(productMap2) !== JSON.stringify(productMap) ){
        alert("同一商品的到货数量累计必须等于原采购数量");
        return false;
    }
    return true;
}

function addBean(opr)
{
    submit('确定提交采购到货信息?', null, lverify);
}


