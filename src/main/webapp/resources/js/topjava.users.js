var ctx;

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "admin/users/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    };
    makeEditable();
});

function checkBoxFunction(input, id) {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + id,
        data: {enable: input.is(":checked")}
    }).done(function () {
        successNoty(input.is(":checked") ? "User enabled" : "User disabled");
        input.closest("tr").attr("data-userEnable", input.is(":checked"));
    }).fail(function (jqXHR) {
        failNoty(jqXHR);
        input.prop("checked", !input.is(":checked"));
    });
}
