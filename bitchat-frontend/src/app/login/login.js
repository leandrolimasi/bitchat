angular
    .module('app')
    .component('login', {
        templateUrl: 'app/login/login.html',
        controller: function ($scope, $rootScope, $http, $log, $state, Notification, $cookies) {
            var ctrl = this;
            
            ctrl.user = $cookies.getObject('user');

            ctrl.login = function(user) {

                $http.post("http://localhost:8080/bitchat/rest/user/login", user).then(function(response) {
                        console.log(response.data);
                        $cookies.putObject('user', response.data);
                        $state.go('home');
                    },

                    function () {
                        Notification.error('Houve um erro na autenticação. Verifique o login e senha!');
                    });

            };
        }
    });

