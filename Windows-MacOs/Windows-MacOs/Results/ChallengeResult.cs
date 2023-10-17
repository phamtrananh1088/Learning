using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace WinMacOs.Results
{
    public class ChallengeResult : IHttpActionResult
    {
        public ChallengeResult(string loginProvider, Controller controller)
        {
            LoginProvider = loginProvider;
            HttpContext = controller.HttpContext;
        }

        public string LoginProvider { get; set; }
        public HttpContextBase HttpContext { get; set; }

        public Task<HttpResponseMessage> ExecuteAsync(CancellationToken cancellationToken)
        {
            HttpContext.GetOwinContext().Authentication.Challenge(LoginProvider);

            HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.Unauthorized);
            response.RequestMessage = HttpContext.Items["MS_HttpRequestMessage"] as HttpRequestMessage;
            return Task.FromResult(response);
        }
    }
}
