function importStockItem(e, data){
    var result = data.result;
    var items = result.msg;
    var ret = result.ret;

    if (ret == 0){
        $("#msg").text(result.msg);
        return;
    }
//            console.log(items);
//            console.log(typeof(items));
    for (var i=0;i<items.length;i++){
        var item = items[i];
//                console.log(item);
        var check = document.getElementById('check_init_' + i);
        check.checked = true;

        var productName = $O('productName_' + i);
//				console.log(productName);
        productName.value=item.productName;

        var productId = $O('productId_' + i);
        productId.value=item.productId;
//				console.log(productId);

        var providerName = $O('providerName_'+i);
        providerName.value=item.providerName;

        var providerId = $O('providerId_'+i);
        providerId.value=item.providerId;

        var price = $O('price_'+i);
        price.value=item.price;
        $d('price_' + i, false);

        var amount = $O('amount_'+i);
        amount.value=item.amount;
        $d('amount_' + i, false);

        var invoiceType = $O('invoiceType_'+i);
        invoiceType.value=item.invoiceType;
//				console.log(invoiceType);
//				console.log(invoiceType.value);
        var invoiceTypeOptions = invoiceType.options;
        for (var j = 0; j < invoiceTypeOptions.length; j++)
        {
//					console.log(invoiceTypeOptions[j].value);
            if (invoiceTypeOptions[j].value == invoiceType.value)
            {
                invoiceTypeOptions[j].selected = "selected";
            }
        }

        var dutyId = $O('dutyId_'+i);
        dutyId.value=item.dutyId;
        var dutyOptions = dutyId.options;
        for (var j = 0; j < dutyOptions.length; j++)
        {
//					console.log(dutyOptions[j].value);
            if (dutyOptions[j].value == dutyId.value)
            {
                dutyOptions[j].selected = "selected";
            }
        }

        var deliveryDate = $O('deliveryDate_'+i);
        deliveryDate.value=item.deliveryDate;

        var arrivalDate = $O('arrivalDate_'+i);
        arrivalDate.value=item.arrivalDate;
    }
}