angular
    .module('app')
    .component('login', {
        templateUrl: 'app/login/login.html',
        controller: function ($scope, $rootScope, $http, $log, $state, Notification) {
            var ctrl = this;

            ctrl.login = function(user) {

                $http.post("http://localhost:8080/bitchat/rest/user/login", user).then(function(response) {
                        $log.info(response);
                    console.log(response.data);
                        $state.go('home');
                        $rootScope.user =  response.data;
                    },

                    function (response) {
                        Notification.error('Houve um erro na autenticação. Verifique o login e senha!');
                    });

            };
        }
    });

