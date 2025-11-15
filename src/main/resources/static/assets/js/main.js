function formattimestamp(strdate)
{
	if(strdate=="") return strdate;
	var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',hour:'numeric',minute:'numeric' };
	return new Date(strdate).toLocaleString('en-us', options);
}
 
function formatdate(strdate)
{
	if(strdate=="") return strdate;
	var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'  };
	return new Date(strdate).toLocaleString('en-us', options);
}

function formatdateshort(strdate)
{
	if(strdate=="") return strdate;
	
	var options = {   year: 'numeric', month: 'long', day: 'numeric'  };
	return new Date(strdate).toLocaleString('en-us', options);
}
  
window._conf = function($msg='',$func='',$params = []){
       $('#confirm_modal #confirm').attr('onclick',$func+"("+$params.join(',')+")")
       $('#confirm_modal .modal-body').html($msg)
       $('#confirm_modal').modal('show')
    }  
    
 window.alert_toast= function($msg = 'TEST',$bg = 'success' ,$pos=''){
	   	 var Toast = Swal.mixin({
	      toast: true,
	      position: $pos || 'top-center',
	      showConfirmButton: false,
	      timer: 5000
	    });
	      Toast.fire({
	        icon: $bg,
	        title: $msg
	      })
	  }   
	  
 function validatemobile(evt)
  {
	  evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
  } 
    function checkEmail(email) { 
    var re = /\S+@\S+\.\S+/;
        return re.test(email);
  }
  
  function  validatealpha(event)
  { 
     var charCode = event.keyCode;

            if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123) || charCode == 8 || charCode == 32 || charCode == 46)

                return true;
            else
                return false;
  }
  	  