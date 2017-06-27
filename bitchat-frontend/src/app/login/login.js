angular
    .module('app')
    .component('app', {
        templateUrl: 'app/login/login.html',
        controller: function ($scope, $rootScope, $http, $log, $state, $state) {
            var ctrl = this;

            ctrl.login = function(user) {

                $http.post("http://localhost:8080/bitchat/rest/user/login", user).then(function(response) {
                        $log.info(response);
                        $state.go('home');
                        $rootScope.user =  response.data;
                    },

                    function (response) {
                        console.log("Error: response from submitting data to server was: " + response.data);
                    });

            };
        }
    });

