angular
    .module('app')
    .service('LoginService', LoginService)
    .component('home', {
        templateUrl: 'app/home/home.html',
        controller: function (BackendUrl, WebsocketUrl, $state, $http, $log, $scope, $rootScope, $stateParams, Notification, $cookies) {
            var ctrl = this;
            ctrl.listContacts = [];
            
            ctrl.user = $cookies.getObject('user');

            ctrl.logout = function() {
            	$cookies.remove('user');
                $state.go('login');
            };
            

        	ctrl.listContacts = function() {
        	        		
        		$http.get(BackendUrl+"/rest/user/contacts/"+ctrl.user.login).then(function (response) {
        				ctrl.listContacts = response.data;
        			},
        			
        			function (response) {
        			      console.log("Error: response from submitting data to server was: " + response);
        			}
        		);
        		

        		
        	};


            ctrl.addContact = function() {
                
                $http.get(BackendUrl+"/rest/user/"+ctrl.contact.login).then(function(response) {
                        var c = {}
                        c.contact = response.data;
                        c.user  = ctrl.user;

                        $http.post(BackendUrl+"/rest/user/addContact/", c).then(function(response) {
                                ctrl.listContacts();
                                ctrl.contact.login = '';
                                Notification.success('Contact added!');
                            },

                            function () {

                                Notification.error('Contact not found!');
                            });

                    },

                    function () {

                        Notification.error('Error in add contact');
                    });

            };

            ctrl.listContacts();
        }
    });