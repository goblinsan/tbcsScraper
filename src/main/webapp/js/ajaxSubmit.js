$(function() {
   $("select").change(function() {
     $(this).parents("form").submit();
   });
 });

$( "#leagueSearch" ).submit(function( event ) {
  event.preventDefault();

  var $form = $( this ),
    sportSelect = $form.find( "select[id='sportSelect']" ).val(),
    daySelect = $form.find( "select[id='daySelect']" ).val(),
    url = $form.attr( "action" );

  var posting = $.post( url, { SPORTSELECT: sportSelect, DAYSELECT: daySelect }, function(responseJson){
        var $select = $('#leagueSearchResult');
        $select.find('option').remove();
        $('<option value="" disabled selected>Select League</option>').appendTo($select);
        $.each(responseJson, function(key, value) {
            $('<option>').val(key).text(value).appendTo($select);
        });
    });
});

$( "#leagueSelect" ).submit(function( event ) {
  event.preventDefault();

  var $form = $( this ),
    leagueSelect = $form.find( "select[id='leagueSearchResult']" ).val(),
    url = $form.attr( "action" );

  var posting = $.post( url, { LEAGUESELECT: leagueSelect}, function(responseJson){
        var $select = $('#teamSearchResult');
        $select.find('option').remove();
        $('<option value="" disabled selected>Select Team</option>').appendTo($select);
        $.each(responseJson, function(key, value) {
            $('<option>').val(key).text(value).appendTo($select);
        });
    });
});
