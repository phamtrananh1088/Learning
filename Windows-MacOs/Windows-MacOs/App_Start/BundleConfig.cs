using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Optimization;
using WinMacOs.Utils.Optimization;

namespace WinMacOs
{
    public class BundleConfig
    {
        // For more information on bundling, visit https://go.microsoft.com/fwlink/?LinkId=301862
        public static void RegisterBundles(BundleCollection bundles)
        {
            bundles.Add(new ScriptBundle("~/bundles/jquery").Include(
                "~/Scripts/jquery-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryval").Include(
                "~/Scripts/jquery.unobtrusive*",
                "~/Scripts/jquery.validate*"));

            // JQuery Comfirm
            bundles.Add(new ScriptBundle("~/bundles/jquery-confirm").Include(
                        "~/Scripts/jquery-confirm.js"));
            bundles.Add(new StyleBundle("~/css/jquery-confirm").Include(
                     "~/Content/jquery-confirm.css"));

            bundles.Add(new ScriptBundle("~/bundles/knockout").Include(
                "~/Scripts/knockout-{version}.js",
                "~/Scripts/knockout.validation.js"));

            //bundles.Add(new ScriptBundle("~/bundles/app").Include(
            //    "~/Scripts/sammy-{version}.js",
            //    "~/Scripts/app/common.js",
            //    "~/Scripts/app/app.datamodel.js",
            //    "~/Scripts/app/app.viewmodel.js",
            //    "~/Scripts/app/home.viewmodel.js",
            //    "~/Scripts/app/_run.js"));
            // 共通JS
            bundles.Add(new ScriptBundle("~/bundles/common").Include(
                        "~/Scripts/common/torihiki.*"));

            // View JS
            bundles.Add(new ScriptBundle("~/bundles/manager/canvas").Include(
                        "~/Scripts/Pages/manager/canvas.js"));
            bundles.Add(new ScriptBundle("~/bundles/manager/notebookelementary").Include(
                       "~/Scripts/Pages/manager/notebookelementary.js"));
            bundles.Add(new ScriptBundle("~/bundles/manager/sqlcompare").Include(
                        "~/Scripts/Pages/manager/sqlcompare.js"));
            bundles.Add(new ScriptBundle("~/bundles/viewer").Include(
                       "~/Scripts/Pages/viewer/viewer.js"));
            bundles.Add(new ScriptBundle("~/bundles/dual").Include(
                       "~/Scripts/Pages/dual/dual.js"));
            bundles.Add(new ScriptBundle("~/bundles/dual/en").Include(
                       "~/Scripts/Pages/dual/en/en.js"));
            // CSS
            bundles.Add(new StyleBundle("~/css/manager/notebookelementary").Include(
                 "~/Content/pages/manager/notebookelementary.css"));
            bundles.Add(new StyleBundle("~/css/manager/sqlcompare").Include(
                 "~/Content/pages/manager/sqlcompare.css"));
            bundles.Add(new StyleBundle("~/css/viewer").Include(
                "~/Content/pages/viewer/viewer.css"));
            bundles.Add(new StyleBundle("~/css/dual").Include(
               "~/Content/pages/dual/dual.css"));
            bundles.Add(new StyleBundle("~/css/dual/en").Include(
                "~/Content/pages/dual/en/en.css"));

            // Use the development version of Modernizr to develop with and learn from. Then, when you're
            // ready for production, use the build tool at https://modernizr.com to pick only the tests you need.
            bundles.Add(new ScriptBundle("~/bundles/modernizr").Include(
                "~/Scripts/modernizr-*"));

            bundles.Add(new ScriptBundle("~/bundles/bootstrap").Include(
                "~/Scripts/bootstrap.js"));
            bundles.Add(new StyleBundle("~/css/bootstrap").Include(
                     "~/Content/bootstrap.css"));

            bundles.Add(new ScriptBundle("~/bundles/fontawesome").Include(
                "~/Scripts/fontawesome-{version}.js"));
            bundles.Add(new ScriptBundle("~/bundles/messages").Include(
                        "~/Scripts/master/GetMessages.js"));

            bundles.Add(new StyleBundle("~/Content/css").Include(
                 "~/Content/bootstrap.css",
                 "~/Content/bootstrap-grid.css",
                 "~/Content/font-awesome.css",
                 "~/Content/Site.css"));
            bundles.Add(new StyleBundle("~/font-awesome/css").Include(
                 "~/Content/font-awesome.css"));
        }
    }
}
