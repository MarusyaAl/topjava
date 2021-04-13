var ctx ,mealAjaxUrl = "profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    ctx = {
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " +  date.substring(11, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                    $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    };
    makeEditable();
});

$('#datetimepicker').datetimepicker({
    format:'d.m.Y H:i',
    inline:false,
    lang:'ru'
});

$('#startDate').datetimepicker({
    format:'d.m.Y',
    timepicker: false,
    inline:false,
    lang:'ru'
});

$('#endDate').datetimepicker({
    format:'d.m.Y',
    timepicker: false,
    inline:false,
    lang:'ru'
});

$('#startTime').datetimepicker({
    format:'H:i',
    datepicker: false,
    inline:false,
    lang:'ru'
});

$('#endTime').datetimepicker({
    format:'H:i',
    datepicker: false,
    inline:false,
    lang:'ru'
});