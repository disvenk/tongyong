var tangshiBaseMix = {
    template: $("#tangshi-temp").html(),
    mixins: [subpageMix],
    data: function () {
        return {
            familyList: [],
            articleMap: new HashMap(),
            articlePriceMap: new HashMap(),
            articleMealsMap: new HashMap(),
            unitPriceShopCart: {},
            currentFamily: {},
            currentArticle: null,
            recommendList: {items: [], count: null},
            unitArr: [],
            unitList: {currentItem: null, item: null},
            allAttr: {},
            unitArticleList: [],
            currentAttrs: [],
            currentUnits: null,
            currentUnitPrice: null,
            isShowShopcart: false,
            isCreateOrder: false,
            allCoupon: [],
            showBindPhone: false,
            couponId: "",
            checkAccount: 1,
            currentCount: 0,
            totalMoney: 0,
            chargeList: [],
            remindDialog: {show: false, articles: []},
            choiceModeDialog: {show: false, mode: null},
            recommendCart: []
        }
    },
    computed: {
        articleList: function () {
            var artList = [];
            for (var i = 0; i < this.familyList.length; i++) {
                for (var j = 0; j < this.familyList[i].articles.length; j++) {
                    var art = this.familyList[i].articles[j];
                    artList.push(art);
                    this.articleMap.put(art.id, art);
                }
            }
            return artList;
        },
        useAccount: function () {
            if (this.checkAccount) {
                return this.customer.account;
            } else {
                return 0;
            }
        },
        unitPriceList: function () {
            var upList = [];
            for (var j = 0; j < this.articleList.length; j++) {
                var ups = this.articleList[j].articlePrices;
                for (var i = 0; i < ups.length; i++) {
                    var up = ups[i];
                    upList.push(up);
                    this.articlePriceMap.put(up.id, up);
                }
            }
            return upList;
        },
        shopCart: function () {
            var cart = {
                items: [],
                totalNumber: 0,
                totalPrice: 0
            };
            var fam = this.familyList;
            for (var j = 0; j < this.articleList.length; j++) {
                var art = this.articleList[j];
                if (art.number > 0 && art.articlePrices.length == 0) { //无规格单品
                    var item = {};
                    item.name = art.name;
                    item.data = art;
                    item.id = art.id;
                    item.type = 1;
                    item.current_working_stock = art.currentWorkingStock;
                    cart.totalNumber += art.number;
                    cart.totalPrice += art.number * parseFloat(art.realPrice);
                    cart.items.push(item);
                }
            }


            for (var i = 0; i < this.unitArticleList.length; i++) {
                var art = this.unitArticleList[i];
                if (art.count > 0) { //无规格单品
                    var item = {};
                    item.name = art.name;
                    item.id = art.id;
                    item.data = art;
                    item.type = 5;
                    cart.totalNumber += art.count;
                    cart.totalPrice += art.count * parseFloat(art.realPrice);
                    cart.items.push(item);
                }
            }


            for (var j = 0; j < this.unitPriceList.length; j++) {  //有规格
                var up = this.unitPriceList[j];
                if (up.number > 0) {
                    var item = {};
                    item.name = up.article.name + up.name
                    item.data = up;
                    item.id = up.id;
                    item.type = 2;
                    item.current_working_stock = up.currentWorkingStock;
                    cart.totalNumber += up.number;
                    cart.totalPrice += up.number * parseFloat(up.realPrice);
                    cart.items.push(item);
                }
            }
            var mealArticles = this.articleMealsMap.values || [];
            for (var j = 0; j < mealArticles.length; j++) {
                var art = mealArticles[j];
                if (art.number > 0) {
                    var item = {};
                    item.name = art.name;
                    item.data = art;
                    item.id = art.id;
                    item.type = 3;
                    item.current_working_stock = art.currentWorkingStock;
                    cart.totalNumber += art.number;
                    cart.totalPrice += art.number * parseFloat(art.realPrice);
                    cart.items.push(item);
                }
            }
            if (cart.items.length == 0) {
                this.isCreateOrder = false;
            }
            return cart;
        },
        useCouponAmount: function () {
            var totalPrice = this.shopCart.totalPrice;
            if (totalPrice > 0) {
                totalPrice -= this.useCoupon;
            } else {
                totalPrice = 0;
            }
            return totalPrice;
        },
        finalAmount: function () {
            var totalPrice = this.useCouponAmount;
            if (totalPrice > 0) {
                totalPrice -= this.useAccount;
            }
            return totalPrice > 0 ? totalPrice : 0;
        },
        canUseCoupon: function () {
            var canUse = [];
            for (var i = 0; i < this.allCoupon.length; i++) {
                var c = this.allCoupon[i];
                if (c.minAmount <= this.shopCart.totalPrice) {
                    if (!c.useWithAccount && this.checkAccount) {
                        continue;
                    }
                    canUse.push(c);
                }
            }
            return canUse;
        },
        useCoupon: function () {
            for (var i = 0; i < this.canUseCoupon.length; i++) {
                var c = this.canUseCoupon[i];
                if (c.id == this.couponId) {
                    return c.value;
                }
            }
            return 0;
        },
        hasRemind: function () {
            console.log("未点提示");
            var arts = [];
            for (var i = 0; i < this.articleList.length; i++) {
                var art = this.articleList[i];
                console.log(art.name, art.isRemind);
                if (art.number == 0 && art.isRemind) {
                    if (art.articlePrices.length == 0 && art.currentWorkingStock > 0) {
                        arts.push(art);
                    } else {
                        var needRemind = true;
                        for (var j = 0; j < art.articlePrices.length; j++) {
                            var price = art.articlePrices[j];
                            if (price.number > 0) {
                                needRemind = false;
                                console.log("已有餐品点过，不需要提示");
                                break;
                            }
                        }
                        if (needRemind) {
                            console.log("没有餐品点过，需要提示");
                            for (var j = 0; j < art.articlePrices.length; j++) {
                                var price = art.articlePrices[j];
                                if (price.currentWorkingStock > 0) {
                                    arts.push(price);
                                }

                            }
                        }
                    }
                }
            }
            return arts.length && arts;
        },
    },
    methods: {
        switchShop: function () {
            this.$dispatch("show-shoplist-dialog");
        },
        moveToAttr: function (attr) {
            var aid = attr.id;
            var dom = $("[data-attr-id='" + aid + "']").get(0);
            if (this.articleIsc) {
                this.articleIsc.scrollToElement(dom);
            }
        },
        findArticleById: function (id) {
            if (id.indexOf("@") > -1) {
                return this.articlePriceMap.get(id);
            } else {
                return this.articleMap.get(id);
            }
        },
        findUnitById: function (unitId) {
            for (var i = 0; i < this.allAttr.length; i++) {
                var attr = this.allAttr[i];
                for (var n = 0; n < attr.articleUnits.length; n++) {
                    var unit = attr.articleUnits[n];
                    if (unit.id == unitId) {
                        return unit;
                    }
                }
            }
        },
        closeShopCart: function () {
            this.isShowShopcart = false;
            this.shopCartIsc = null;
        },
        showShopCart: function () {
            this.isShowShopcart = true;
            var that = this;
            Vue.nextTick(function () {
                var cartlist = $(that.$el).find(".shopcart-list");
                var parentH = cartlist.parent().height();
                cartlist.siblings().each(function () {
                    parentH -= $(this).height();
                });
                cartlist.height(parentH);
                that.shopCartIsc = new IScroll(cartlist.get(0), {
                    click: iScrollClick(),
                    scrollbars: true,
                });
            });
        },
        clearShopCart: function () {
            var shopcart = this.shopCart;
            for (var i = 0; i < shopcart.items.length; i++) {
                var art = shopcart.items[i];
                if (art.number) {
                    art.number = 0;
                }
                if (art.data) {
                    art.data.number = 0;
                }
            }
            clearShopcartApi();
        },
        updateToServerCart: function (unitOrArt) {
            var id = unitOrArt.id;
            updateShopCart(id, 1, unitOrArt.number);
        },
        addToShopCart: function (a) {
            a.number++;
            this.totalMoney += a.price;
            this.updateToServerCart(a);
        },
        addToNewShopCart: function () {
            if (this.currentCount > this.recommendList.count) {
                this.$dispatch("message", "超过配餐最大购买数量", 81000);
                return;
            }

            for (var i = 0; i < this.unitArr.length; i++) { //遍历规格
                if (this.unitArr[i].choiceType == 0) { //单选
                    if (this.unitArr[i].currentItem.length == 0) {
                        this.$dispatch("message", "请选择" + this.unitArr[i].name, 81000);
                        return;
                    }
                }
            }


            for (var i = 0; i < this.recommendCart.length; i++) {
                if (this.recommendCart[i].key == this.currentArticle.id) {
                    this.recommendCart[i].item = [];
                }
            }


            //var c = false;
            //var n;
            //var item = this.unitList.item;
            //for(var i = 0;i< item.length;i++){
            //    if(item[i].type == 0){ //必选属性
            //        for(var k = 0 ;k < item[i].detailList.length;k++){
            //            if(item[i].detailList[k].click){
            //                c = true;
            //                n = item[i].detailList[k].name;
            //                break;
            //            }
            //        }
            //        if(!c){
            //            n = item[i].name;
            //        }
            //
            //    }
            //    if(c){
            //        break;
            //    }
            //}
            //if(!c){
            //    this.$dispatch("message", n + "为必选项", 81000);
            //    return;
            //}

            var list = this.recommendList;
            var artList = this.articleList;
            var value = [];
            for (var i = 0; i < list.items.length; i++) { //遍历推荐餐品列表
                var obj = list.items[i];
                var no = obj.count;
                if (obj.count > 0) {//判断是有存在购买的推荐商品
                    value.push(obj);
                    for (var u = 0; u < artList.length; u++) {
                        if (artList[u].id == obj.articleId) {
                            artList[u].number += obj.count;
                            no = artList[u].number;
                        }
                    }
                    updateShopCart(obj.articleId, 1, no);
                }

            }

            var recommend = {
                key: this.currentArticle.id,
                item: value
            }
            this.recommendCart = [recommend];


            if (this.unitArr.length > 0) { //有规格单品
                var sum = this.currentArticle.price;
                var orgPrice = this.currentArticle.price;
                var orgName = this.currentArticle.name;
                var name = this.currentArticle.name;
                var currentList = [];

                for (var i = 0; i < this.unitArr.length; i++) {
                    for (var t = 0; t < this.unitArr[i].currentItem.length; t++) {
                        currentList.push(this.unitArr[i].currentItem[t]);
                        name = name + "(" + this.unitArr[i].currentItem[t].name + ")";
                        sum += this.unitArr[i].currentItem[t].price;
                    }
                }

                var contains = false;
                for (var i = 0; i < this.unitArticleList.length; i++) {
                    if (this.unitArticleList[i].id == this.currentArticle.id) {
                        contains = true;
                        this.unitArticleList[i].realPrice = sum;
                        this.unitArticleList[i].name = name;
                    }
                }
                if (!contains) {
                    this.unitArticleList.push({
                        name: name,
                        id: this.currentArticle.id,
                        realPrice: sum,
                        orgPrice: orgPrice,
                        orgName: orgName,
                        unitList: currentList,
                        count: 1,
                        showBig: this.currentArticle.showBig,
                        photoSmall: this.currentArticle.photoSmall,
                        description: this.currentArticle.description

                    });
                }


                //this.currentArticle.number++;
                //this.updateToServerCart(this.currentArticle);
                //this.shopCart.totalNumber = 111;
            } else { //无规格单品
                this.currentArticle.number++;
                this.updateToServerCart(this.currentArticle);
            }


            this.currentArticle = null;
            //this.closeAction();


        },
        cutToShopCart: function (a) {
            this.totalMoney -= a.price;
            if (a.number > 0) {
                a.number--;
                this.updateToServerCart(a);
            }
            if (a.number == 0) {
                this.shopCartIsc && this.shopCartIsc.refresh();
            }
        },
        cutToShopCartNew: function (a) {
            this.totalMoney -= a.realPrice;
            if (a.count > 0) {
                a.count--;
                this.updateToServerCart(a);
            }
            if (a.count == 0) {
                this.shopCartIsc && this.shopCartIsc.refresh();
            }
        },
        saveMeals: function (art) {
            var saveArt = $.extend(true, {}, art);
            if (!saveArt.tempId) {
                saveArt.tempId = saveArt.id + "@" + new Date().getTime();
                console.log("添加套餐");
            }
            saveArt.number = 1;
            this.articleMealsMap.put(saveArt.tempId, saveArt);
            this.$set("articleMealsMap.values", this.articleMealsMap.getValues());
            this.closeAction();

            console.log(this.shopCart.totalNumber);
        },
        getMealsReadPrice: function (art) {
            var og = art.fansPrice || art.price;
            for (var i = 0; i < art.mealAttrs.length; i++) {
                var attr = art.mealAttrs[i];
                for (var k = 0; k < attr.currentItem.length; k++) {
                    og += attr.currentItem[k].priceDif;
                }

            }
            art.realPrice = og.toFixed(2);
            return og.toFixed(2);
        },
        selectMealItem: function (item, attr) {
            if (attr.choiceType == 0) {
                attr.currentItem = [];
                attr.currentItem[0] = item;
                //单选
                for(var i =0;i< attr.mealItems.length;i++){
                    if(attr.mealItems[i].click){
                        attr.mealItems[i].click = false;
                    }

                }
                item.click = true;
            } else {



                if (!attr.currentItem) {
                    attr.currentItem = [];
                    attr.currentItem.push(item);
                } else {

                    if(attr.currentItem.length == 1 && item.click){
                        this.$dispatch("message", attr.name +"为必选", 81000);
                        return;
                    }


                    if(item.click){
                        attr.currentItem.$remove(item);
                    }else{
                        attr.currentItem.push(item);
                    }

                }
                if(item.click){
                    item.click = false;

                }else{
                    item.click =true;
                }
            }

        },
        selectUnitItem: function (item, attr) {
            if (attr.choiceType == 0) { //单选
                if (attr.currentItem.length == 0) { //未选择
                    attr.currentItem.push(item);
                    item.click = true;
                    this.totalMoney += item.price;
                } else { //选择的情况
                    if (attr.currentItem[0].id == item.id) { //选的是自己
                        this.$dispatch("message", attr.name + "为必选项", 81000);
                    } else {
                        attr.currentItem[0].click = false;
                        this.totalMoney -= attr.currentItem[0].price;
                        attr.currentItem = [];
                        attr.currentItem.push(item);
                        item.click = true;
                        this.totalMoney += item.price;
                    }

                }
            } else { //多选
                var has = false;
                for (var i = 0; i < attr.currentItem.length; i++) {
                    if (attr.currentItem[i].id == item.id) {
                        has = true;
                        item.click = false;
                        attr.currentItem.$remove(item);
                        this.totalMoney -= item.price;
                    }
                }
                if (!has) {
                    this.totalMoney += item.price;
                    attr.currentItem.push(item);
                    item.click = true;
                }
            }
            //if (attr.currentItem) {
            //    if(attr.currentItem.id == item.id){
            //        if(attr.choiceType == 0 ){
            //            this.$dispatch("message",attr.name + "为必选项", 81000);
            //        }else{
            //            this.totalMoney -= item.price;
            //            attr.currentItem = null;
            //        }
            //
            //    }else{
            //        this.totalMoney += item.price;
            //        attr.currentItem = item;
            //    }
            //
            //} else {
            //    this.totalMoney += item.price;
            //    attr.currentItem = item;
            //}
        }


        ,
        selectRecommend: function (item) {
            if (item.count < item.maxCount) {
                this.totalMoney += item.price;
                item.count++;
                this.currentCount++;
            } else {
                this.totalMoney -= item.maxCount * item.price;
                this.currentCount -= item.maxCount;
                this.$dispatch("message", "此商品最大购买" + item.maxCount + "个", 81000);
                item.count = 0;
            }


        }
        ,
        selectUnit: function (item, attr) {

            if (attr.type == 0) { //单选
                for (var i = 0; i < attr.detailList.length; i++) {
                    if (attr.detailList[i].click && attr.detailList[i].id != item.id) {
                        attr.detailList[i].click = false;
                        this.totalMoney -= attr.detailList[i].spread;
                    }
                }

            } else { //多选或不选

            }

            if (item.click) { //选中
                item.click = false;
                this.totalMoney -= item.spread;
            } else {  //没有选中
                item.click = true;
                this.totalMoney += item.spread;
            }

        }
        ,
        showActionNew: function (a) {
            this.currentArticle = a;
            this.currentArticle.articleType = 1;
            this.currentArticle.showDesc = true;
            this.currentArticle.price = a.orgPrice;
            this.currentArticle.name = a.orgName;

            $.ajax({
                type: "post",
                url: "order/getRecommend",
                data: {
                    articleId: a.id
                },
                success: function (result) {
                    that.recommendList.items = [];
                    that.recommendList.count = result.count;
                    if (result.articles) {
                        for (var i = 0; i < result.articles.length; i++) {
                            //var ct = 0;
                            //for(var j = 0;j < t.length;j++){
                            //    if (t[j].articleId == result.articles[i].articleId){ //如果该菜品被选中过
                            //        ct = t[j].count;
                            //    }
                            //}
                            var stock = null;
                            for (var j = 0; j < that.articleList.length; j++) {
                                var b = that.articleList[j];
                                if (b.id == result.articles[i].articleId) {
                                    stock = that.articleList[j].currentWorkingStock;
                                    break;
                                }
                            }


                            that.recommendList.items.push({
                                articleId: result.articles[i].articleId,
                                price: result.articles[i].price,
                                articleName: result.articles[i].articleName,
                                count: 0,
                                maxCount: result.articles[i].maxCount,
                                currentWorkingStock: stock
                            });

                        }
                    }

                }

            });


            $.ajax({
                type: "post",
                url: "order/getUnit",
                data: {
                    articleId: a.id
                },
                success: function (result) {
                    that.unitArr = [];

                    for (var i = 0; i < result.length; i++) {
                        var details = [];
                        for (var k = 0; k < result[i].details.length; k++) {
                            var detail = {
                                id: result[i].details[k].id,
                                name: result[i].details[k].name,
                                click: false,
                                price: result[i].details[k].price
                            }

                            details.push(detail);
                        }

                        that.unitArr.push({
                            currentItem: [],
                            name: result[i].name,
                            choiceType: result[i].choiceType,
                            sort: result[i].sort,
                            details: details

                        });
                    }


                }

            });

            //this.recommendList.push({
            //    articleId: 1,
            //    price: 2,
            //    articleName: 3
            //});
            if (a.hasUnit.length > 0 || a.articleType == 2) {
                for (var i = 0; i < this.allAttr.length; i++) {
                    var attr = this.allAttr[i];
                    var units = attr.articleUnits;
                    if (!units) {
                        continue;
                    }
                    var cAttr = {id: attr.id, name: attr.name, units: []};
                    for (var n = 0; n < units.length; n++) {
                        var un = units[n];
                        if ($.inArray(un.id.toString(), a.hasUnit) > -1) {
                            un.attr_id = attr.id;
                            cAttr.units.push(un);
                        }
                    }
                    if (cAttr.units.length > 0) {
                        this.currentAttrs.push(cAttr);
                    }
                }
                var that = this;
                Vue.nextTick(function () {
                    var dis = $(that.$el).find(".dish_size_bg");
                    var totalHeight = dis.parent().height();
                    var otherHeight = 0;
                    dis.siblings().each(function () {
                        var he = $(this).outerHeight(true);
                        otherHeight += he;
                    })
                    var remainHeight = totalHeight - otherHeight;
                    var h = dis.height();
                    dis.height(remainHeight);
                    if (dis.height() < h) {
                        dis.css({
                            overflow: "hidden"
                        });
                        that.articleIsc = new IScroll(dis.get(0), {
                            probeType: 2,
                            click: iScrollClick(),
                        });
                    }
                });
            }
        },
        showAction: function (a) {
            this.currentArticle = a;
            this.totalMoney = a.price;
            this.currentCount = 0;
            if(a.articleType == 2){
                for(var j in a.mealAttrs){
                    var attr = a.mealAttrs[j];
                    var hasDefault = false;
                    for(var n in attr.mealItems){
                        var item = attr.mealItems[n];
                        if(item.isDefault){
                            attr.currentItem=[];
                            attr.currentItem[0] = item;
                            hasDefault=true;
                            item.click =true;
                        }else{
                            item.click = false;
                        }

                        item.photoSmall = getPicUrl(item.photoSmall);
                    }
                    if(!hasDefault){
                        if(attr.mealItems != null && attr.mealItems.length > 0 ){
                            attr.currentItem = [];
                            attr.currentItem[0] = attr.mealItems[0];
                            attr.mealItems[0].click = true;
                        }

                    }
            }
            }


            var that = this;

            Vue.nextTick(function () {
                var cartlist = $(that.$el).find(".dish_size_bg .meals-wrap .ssjs");
                var parentH = cartlist.parent().height();
                cartlist.siblings().each(function () {
                    parentH -= $(this).height();
                });
                cartlist.height(parentH);
                that.shopCartIsc = new IScroll(cartlist.get(0), {
                    click: iScrollClick(),
                    scrollbars: true,
                });
            });
            //var t =[];
            //for(var i = 0;i< this.recommendCart.length;i++){
            //    if(this.recommendCart[i].key == a.id){ //拿到该菜品的推荐包
            //        t =  this.recommendCart[i].item;
            //        for(var k = 0;k < t.length;k++){
            //            this.totalMoney += t[k].price * t[k].count;
            //        }
            //
            //    }
            //}
            $.ajax({
                type: "post",
                url: "order/getRecommend",
                data: {
                    articleId: a.id
                },
                success: function (result) {
                    that.recommendList.items = [];
                    that.recommendList.count = result.count;
                    if (result.articles) {
                        for (var i = 0; i < result.articles.length; i++) {
                            //var ct = 0;
                            //for(var j = 0;j < t.length;j++){
                            //    if (t[j].articleId == result.articles[i].articleId){ //如果该菜品被选中过
                            //        ct = t[j].count;
                            //    }
                            //}
                            var stock = null;
                            for (var j = 0; j < that.articleList.length; j++) {
                                var b = that.articleList[j];
                                if (b.id == result.articles[i].articleId) {
                                    stock = that.articleList[j].currentWorkingStock;
                                    break;
                                }
                            }


                            that.recommendList.items.push({
                                articleId: result.articles[i].articleId,
                                price: result.articles[i].price,
                                articleName: result.articles[i].articleName,
                                count: 0,
                                maxCount: result.articles[i].maxCount,
                                currentWorkingStock: stock
                            });

                        }
                    }

                }

            });


            $.ajax({
                type: "post",
                url: "order/getUnit",
                data: {
                    articleId: a.id
                },
                success: function (result) {
                    that.unitArr = [];

                    for (var i = 0; i < result.length; i++) {
                        var details = [];
                        for (var k = 0; k < result[i].details.length; k++) {
                            var detail = {
                                id: result[i].details[k].id,
                                name: result[i].details[k].name,
                                click: false,
                                price: result[i].details[k].price
                            }

                            details.push(detail);
                        }

                        that.unitArr.push({
                            currentItem: [],
                            name: result[i].name,
                            choiceType: result[i].choiceType,
                            sort: result[i].sort,
                            details: details

                        });
                    }


                }

            });

            //this.recommendList.push({
            //    articleId: 1,
            //    price: 2,
            //    articleName: 3
            //});
            if (a.hasUnit.length > 0 || a.articleType == 2) {
                for (var i = 0; i < this.allAttr.length; i++) {
                    var attr = this.allAttr[i];
                    var units = attr.articleUnits;
                    if (!units) {
                        continue;
                    }
                    var cAttr = {id: attr.id, name: attr.name, units: []};
                    for (var n = 0; n < units.length; n++) {
                        var un = units[n];
                        if ($.inArray(un.id.toString(), a.hasUnit) > -1) {
                            un.attr_id = attr.id;
                            cAttr.units.push(un);
                        }
                    }
                    if (cAttr.units.length > 0) {
                        this.currentAttrs.push(cAttr);
                    }
                }
                var that = this;
                Vue.nextTick(function () {
                    var dis = $(that.$el).find(".dish_size_bg");
                    var totalHeight = dis.parent().height();
                    var otherHeight = 0;
                    dis.siblings().each(function () {
                        var he = $(this).outerHeight(true);
                        otherHeight += he;
                    })
                    var remainHeight = totalHeight - otherHeight;
                    var h = dis.height();
                    dis.height(remainHeight);
                    if (dis.height() < h) {
                        dis.css({
                            overflow: "hidden"
                        });
                        that.articleIsc = new IScroll(dis.get(0), {
                            probeType: 2,
                            click: iScrollClick(),
                        });
                    }
                });
            }
        }
        ,
        changeCurrentFamily: function (f) {
            this.currentFamily = f;
            var that = this;
            var fid = this.currentFamily.id;
            if (that.artListIsc) {
                var fidDom = $(that.$el).find("[data-family-id='" + fid + "']");
                that.artListIsc.scrollToElement(fidDom.get(0));
            }
        }
        ,
        closeAction: function () {
            this.currentArticle = null;
            this.currentAttrs = [];
            this.currentUnits = null;
            this.currentUnitPrice = null;
        }
        ,
        hasUnit: function (unitId) {
            if (this.currentUnits && this.currentUnits.values.length > 0 && this.currentUnits.containsValue(unitId)) {
                return true;
            }
            return false;
        }
        ,
        hasFansPrice: function (article) {
            var ups = article.articlePrices;
            if (ups.length == 0) {
                return false;
            }
            var fans_price = null;
            for (var i = 0; i < ups.length; i++) {
                var up = ups[i];
                var fans = 0;
                if (fans_price == null && up.fansPrice) {
                    fans_price = up.fansPrice;
                } else if (up.fansPrice && fans_price != null && up.fansPrice < fans_price) {
                    fans_price = up.fansPrice;
                }

            }
            return fans_price;
        }
        ,
        choiceUnit: function (unit) {
            if (!this.currentUnits) {
                var map = new HashMap();
                map.price = 0;
                map.unit_price = 0;
                map.attr = 0;
                map.values = [];
                this.currentUnits = map;
            }
            this.currentUnits.put(unit.attr_id, unit.id.toString());
            this.currentUnits.values = this.currentUnits.getValues();
            for (var n = 0; n < this.currentArticle.articlePrices.length; n++) {
                var unit = this.currentArticle.articlePrices[n];
                var isThis = true;
                for (var i = 0; i < unit.ids.length; i++) {
                    var unit_id = unit.ids[i];
                    if (!this.currentUnits.containsValue(unit_id)) {
                        isThis = false;
                        break;
                    }
                }
                if (isThis) {
                    this.currentUnitPrice = unit;
                    break;
                } else {
                    this.currentUnitPrice = null;
                }
            }
        }
        ,
        showDialog: function (msg) {
            this.$dispatch("message", msg);
        }
        ,
        closeRemindDialog: function () {
            this.remindDialog.show = false;
            this.remindDialog.articles = [];
        }
        ,
        remindDialogOk: function () {
            this.closeRemindDialog();
            this.showCreateOrder(false);
        }
        ,
        closeShopCartAndShowCreate: function () {
            //close shopcart
            if (this.isShowShopcart = true) {
                this.isShowShopcart = false;
            }
            //execute showCreateOrder(true)
            this.showCreateOrder();

        }
        ,
        showCreateOrder: function (showRemind, afterShow) {
            if (this.shopCart.totalNumber == 0) {
                this.showDialog("您还没有点餐哦！");
                this.closeCreateOrder();
            } else {
                 if(this.shop.id == "d89d1a7ef12346bfb0ef92faba8872af"&& !this.choiceModeDialog.mode){//默认上匠有堂吃和外卖选项
                    this.choiceModeDialog.show = true;
                     return false;
                }else{
                    if (allSetting.isChoiceMode && !this.choiceModeDialog.mode) {  //是否手动选择模式
                        this.choiceModeDialog.show = true;
                        return false;
                   }
              }
                if (showRemind && this.hasRemind) {
                    this.remindDialog.show = true;
                    this.remindDialog.articles = this.hasRemind;
                    this.$nextTick(function () {
                        var list = $(this.$el).find(".remind-list");
                        var pH = list.parent().height();
                        list.siblings().each(function () {
                            pH -= $(this).height();
                        })
                        list.height(pH);
                        new IScroll(list.get(0), {
                            probeType: 2,
                            click: iScrollClick(),
                        });
                    });
                    return false;
                }

                this.$dispatch("loading");
                var that = this;
                getCouponList(1, function (coupon) {
                    that.$dispatch("loaded");
                    that.allCoupon = coupon;
                    that.isCreateOrder = true;
                    if(that.customer.account <= 0){//如果当前用户余额为零，默认不使用余额
                    	that.checkAccount = 0;
                    }
                    Vue.nextTick(function () {
                        setTimeout(function () {
                            that.reflushOrderList();
                            if (that.canUseCoupon.length > 0 && !that.couponId) {
                                that.couponId = that.canUseCoupon[0].id;
                            }
                        }, 50);
                        afterShow && afterShow();
                    });
                });


                //$.ajax({
                //	url : "customer/info",
                //	success : function(user) {
                //		//alert(JSON.stringify(user));
                //		var u = user.data;
                //		debugger;
                //		if(u != null && u.telephone && u.isBindPhone){
                //				getCouponList(1,function(coupon){
                //					debugger;
                //					that.$dispatch("loaded");
                //					that.allCoupon = coupon;
                //					that.isCreateOrder=true;
                //					Vue.nextTick(function(){
                //						setTimeout(function(){
                //							that.reflushOrderList();
                //							if(that.canUseCoupon.length>0&&!that.couponId){
                //								that.couponId = that.canUseCoupon[0].id;
                //							}
                //						},50);
                //						afterShow&&afterShow();
                //					});
                //				});
                //		}
                //		//sbc && sbc(user.data);
                //	}
                //});
                //getCustomer(function(customer){
                //	that.customer=customer;
                //	if(!that.customer.isBindPhone||!that.customer.telephone){
                //		that.$dispatch("bind-phone");
                //		that.$dispatch("loaded");
                //		return;
                //	}
                //	getCouponList(1,function(coupon){
                //		that.$dispatch("loaded");
                //		that.allCoupon = coupon;
                //		that.isCreateOrder=true;
                //		Vue.nextTick(function(){
                //			setTimeout(function(){
                //				that.reflushOrderList();
                //				if(that.canUseCoupon.length>0&&!that.couponId){
                //					that.couponId = that.canUseCoupon[0].id;
                //				}
                //			},50);
                //			afterShow&&afterShow();
                //		});
                //	});
                //
                //});
            }
        }
        ,
        closeCreateOrder: function () {
            this.isCreateOrder = false;
        }
        ,
        createOrder: function () {
            var orderForm = {};
            if (this.otherdata && this.otherdata.event == "continue-order") {
                var parentOrder = this.otherdata;
                orderForm.parentOrderId = parentOrder.id;
                orderForm.tableNumber = parentOrder.tableNumber;
            } else if (getParam("tableNumber")) {
                var tableNumber = getParam("tableNumber");
                orderForm.tableNumber = tableNumber;
            }
            orderForm.customerId = this.customer.id;
            orderForm.distributionModeId = 1;
            if (this.canUseCoupon && this.canUseCoupon.length > 0 && this.couponId) {
                orderForm.useCoupon = this.couponId + "";
            }
            if (this.useAccount) {
                orderForm.useAccount = true;
            } else {
                orderForm.useAccount = false;
            }
            orderForm.orderItems = [];
            for (var i = 0; i < this.shopCart.items.length; i++) {
                var item = this.shopCart.items[i];
                var orderItem;
                if (item.type == 5) {
                    orderItem = {
                        articleId: item.id,
                        count: 1,
                        type: item.type,
                        name: item.name,
                        price: item.data.realPrice
                    };
                } else {
                    orderItem = {
                        articleId: item.id,
                        count: item.data.number,
                        type: item.type
                    };
                }

                if (item.type == 3) {
                    orderItem.mealItems = [];
                    for (var n = 0; n < item.data.mealAttrs.length; n++) {
                        var mealAttr = item.data.mealAttrs[n];
                        for (var k = 0; k < mealAttr.currentItem.length; k++) {
                            orderItem.mealItems.push(mealAttr.currentItem[k].id);
                        }

                    }
                }
                orderForm.orderItems.push(orderItem);
            }
            orderForm.paymentAmount = this.finalAmount;

            if (allSetting.isChoiceMode && this.choiceModeDialog.mode) {
                orderForm.distributionModeId = this.choiceModeDialog.mode;
             }
             else if(this.shop.id=='d89d1a7ef12346bfb0ef92faba8872af' && this.choiceModeDialog.mode){
                 orderForm.distributionModeId=this.choiceModeDialog.mode;
             }
            //如果没有绑定手机号  且使用了余额买单，则弹出绑定手机号码页面
            if (this.checkAccount == true && this.customer.isBindPhone == false && this.customer.account > 0) {
                console.log("提示注册");
                this.isCreateOrder = false;
                this.$dispatch("bind-phone");
                return;
            }
            console.log("已注册");
            if (this.finalAmount == 0) {
                this.$dispatch("save-order", orderForm);
            } else {
                console.log("需要支付金额大于0,打开微信支付弹窗");
                this.$dispatch("show-wechat-pay", orderForm);
            }
        }
        ,
        reflushArticleList: function () {
            if (this.artListIsc) {
                this.artListIsc.reflush();
                this.famListIsc.reflush();
            } else {
                var dom = $(this.$el);
                this.artListIsc = new IScroll(dom.find(".article-list-wapper").get(0), {
                    probeType: 2,
                    click: iScrollClick(),
                });
                this.famListIsc = new IScroll(dom.find(".article-family-list").get(0), {
                    click: iScrollClick(),
                });
                var that = this;
                var beforId = null;
                var familyId = null;
                this.artListIsc.on("scroll", function () {
                    dom.find(".article-family-group").each(function () {
                        var dom = $(this);
                        var position = dom.position();
                        if (position.top >= 0) {
                            return false;
                        }
                        familyId = dom.data("family-id");
                    });
                    that.currentFamily = {id: familyId};
                });
            }
        }
        ,
        reflushOrderList: function () {

            var shopCartItems = $(this.$el).find(".shop-cart-items");
            var wH = $(window).height();
            var opH = $(this.$el).find(".order-operator-div").height();
            var totalPrice = $(this.$el).find(".total-price");
            var pH = totalPrice.height();
            var maxH = wH - opH - 10 - pH;

            /**
             * 如果高度不够，则添加bottom
             */
            if (shopCartItems.children().height() < maxH) {
                totalPrice.css({
                    marginBottom: maxH - shopCartItems.children().height()
                });
            } else {
                shopCartItems.css({
                    maxHeight: maxH
                });
            }

            new IScroll(shopCartItems.get(0), {
                click: iScrollClick(),
            });

//				var orderList = $(this.$el).find(".order-cart-list");
//				new IScroll(orderList.get(0),{
//					scrollbars:true
//				});

        }
        ,
        useChecked: function (e) {
            this.checkAccount = !this.checkAccount;
        }
        ,
        getPrice: function (article) {
            return article.price;
            //if(article.articlePrices.length==0){
            //
            //}else{
            //	var min = null;
            //	for(var i=0;i<article.articlePrices.length;i++){
            //		var up = article.articlePrices[i];
            //		if(min==null||min>up.price){
            //			min = up.price;
            //		}
            //	}
            //	return min;
            //}
        }
    },
    created: function () {
        var that = this;
        this.$on("pay-success", function () {
            getArticleFamily(function (fam) {
                that.familyList = fam;
            }, {modeid: 1, attr: that.allAttr});
            that.closeCreateOrder();
        });

        this.$on("charge-success", function () {
            that.showCreateOrder();
        });

        this.$on("bind-success", function () {
            that.showCreateOrder(null);
        });

        this.$dispatch("loading");
        //加载所有规格
        getAllAttr(function (attr) {
            that.allAttr = attr;
            //加载所有菜品
            getArticleFamily(function (fam) {
                that.familyList = fam;
                that.currentFamily = fam[0];
                that.$dispatch("loaded");
                //加载购物车
                loadShopCart(1, function (shopcart) {
                    for (var i = 0; i < shopcart.length; i++) {
                        var c = shopcart[i];
                        var art_id = c.articleId;
                        var u_art = that.findArticleById(art_id);
                        if (u_art) {
                            u_art.number = c.number;
                        }
                    }
                });
                setTimeout(function () {
                    that.reflushArticleList();
                }, 50);
            }, {modeid: 1, attr: attr});
        });

        getChargeList(function (chargeRules) {
            that.chargeList = chargeRules;
        });
    },
    attached: function () {
        var dom = $(this.$el);
        setTimeout(function () {
            var parentHeight = dom.parent().height();
            var logoHeight = dom.find(".menuLogo").height();
            $(".article-container").height(parentHeight - logoHeight);
        }, 50);
        var that = this;
    },
    events: {
        "choiceMode": function (mode) {
            console.log("choiceSuccess");
            console.log(mode+"方式")
            this.choiceModeDialog.mode = mode;
            this.choiceModeDialog.show = false;
            this.showCreateOrder();
        }
    },
    components: {
        "choice-mode-dialog": {
            mixins: [dialogMix],
            template: '<div class="weui_dialog_confirm" v-if="show">                                    ' +
            '	<div class="weui_mask" @click="close"></div>                                                ' +
            '	<div class="weui_dialog transparent choice-mode">                                  ' +
            '		<a  class="weui_btn weui_btn_default" @click="choiceMode(3)">外带</a>' +
            '		<a  class="weui_btn weui_btn_primary" @click="choiceMode(1)">堂吃</a>' +
            '	</div>                                                                              ' +
            '</div>                                                                                 ',
            methods: {
                choiceMode: function (mode) {
                    this.$dispatch("choiceMode", mode);
                }
            }

        },
        "article-operator": {
            props: ["article", "style"],
            template: '<span class="numberControl" :style="style" v-if="!article.isEmpty">                                                                                  ' +
            '	<span v-if="article.number" @click.stop="cutA" class="cut-span">   <i class="icon iconfont minus">&#xe751;</i></span>' +
            '	<span v-if="article.number" class="art-number">{{article.number}}</span>                                   ' +
            '	<span class="plus-span" @click.stop="addArticle"><i class="icon iconfont plus">&#xe6cd;</i></span>                         ' +
            '</span>                                                                                                       ' +
            '<span class="numberControl font-15" :style="style" v-else>已售罄</span>',
            created: function () {
                if (this.article.isEmpty) {
                    this.article.number = 0;
                }
            },
            methods: {
                cutA: function (e) {
                    this.$dispatch('cut', this.article);
                },
                addArticle: function (e) {
                    var clickAnt = $("<span>1</span>");
                    clickAnt.css({
                        fontSize: "18px",
                        padding: "0px 10px",
                        backgroundColor: "#CDCDCD",
                        color: "white",
                        borderRadius: "50%",
                        zIndex: 99999
                    });
                    var s = $(".art-count").offset();
                    var a = $(e.target).offset();
                    var speed = 2 + 2 / (s.top - a.top) * 20;
                    clickAnt.fly({
                        start: {
                            left: a.left,
                            top: a.top
                        },
                        end: {
                            left: s.left,
                            top: s.top,
                        },
                        autoPlay: true,
                        speed: speed,
                        vertex_Rtop: a.top - 50,
                        onEnd: function () {
                            clickAnt.remove();
                        }
                    });

                    this.$dispatch('add', this.article);
                }
            }
        },
    }

};