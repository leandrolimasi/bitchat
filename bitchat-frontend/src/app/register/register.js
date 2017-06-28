angular
    .module('app')
    .component('register', {
        templateUrl: 'app/register/register.html',
        controller: function ($http, Notification, BackendUrl, $state) {
            var ctrl = this;

            ctrl.register = function(user) {
                $http.post(BackendUrl+"/rest/user/register", user).then(function(response) {

                        Notification.success('Sucessful register a new user!');
                        $state.go('login');

                    },

                    function (response) {
                        Notification.error('Error registering user!');
                    });

            };
        }
    });


function LoginService($http) {

}