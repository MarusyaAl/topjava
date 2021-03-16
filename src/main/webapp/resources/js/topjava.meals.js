var ctx;

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "profile/meals/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    };
    makeEditable();
});

function filterByDate() {
    $.get({
        url: ctx.ajaxUrl + "filter",
        data: filterForm.serialize()
    }).done(function (data) {
        reDrawTable(data);
    });
}

function resetFilter() {
    filterForm[0].reset();
    $.get({
        data: filterForm.serialize()
    }).done(function (data) {
        reDrawTable(data);
    });
}
