var dateSearch = {
  
  data() {
      return {
          
      }
  },
  created: function () {

  },
  // template: template,
  // mixins: [vueObj],
  methods: {
    // 查询今日
    today : function(){
      // date = new Date().format("yyyy-MM-dd");
      this.searchDate.beginDate = new Date().format("yyyy-MM-dd 00:00:00");
      this.searchDate.endDate = new Date().format("yyyy-MM-dd hh:mm:ss");
      this.searchInfo(this.searchDate.beginDate, this.searchDate.endDate);
    },
    // 查询昨日
    yesterDay : function(){
        this.searchDate.beginDate = GetDateStr(-1) +" 00:00:00";
        this.searchDate.endDate  = GetDateStr(-1) +" 23:59:59";
        this.searchInfo(this.searchDate.beginDate, this.searchDate.endDate);
    },
    // 查询本周
    week : function(){
        this.searchDate.beginDate  = getWeekStartDate() + ' 00:00:00';
        this.searchDate.endDate  = new Date().format("yyyy-MM-dd") + ' 23:59:59';
        this.searchInfo(this.searchDate.beginDate, this.searchDate.endDate);
    },
    // 查询本月
    month : function(){
        this.searchDate.beginDate  = getMonthStartDate() + ' 00:00:00';
        this.searchDate.endDate  = new Date().format("yyyy-MM-dd") + ' 23:59:59';
        this.searchInfo(this.searchDate.beginDate, this.searchDate.endDate);
    },
  }

}