angular
    .module('app')
    .service('LoginService', LoginService)
    .component('app', {
        templateUrl: 'app/login/login.html',
        controller: function (LoginService) {
            var ctrl = this;

            ctrl.login = function(hero, prop, value) {
                hero[prop] = value;
            };
        }
    });


function LoginService($http) {

}