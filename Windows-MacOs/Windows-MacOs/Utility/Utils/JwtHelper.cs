using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;

namespace WinMacOs.Utility.Utils
{
    public class JwtHelper
    {
        /// <summary>
        /// JWT作成
        /// </summary>
        /// <param name="userInfo"></param>
        /// <returns></returns>
        public static string IssueJwt(UserInfo userInfo)
        {
            string exp = $"{new DateTimeOffset(DateTime.Now.AddMinutes(AppSetting.ExpMinutes)).ToUnixTimeSeconds()}";
            var claims = new List<Claim>
            {
                //(JWT ID) 
                new Claim(JwtRegisteredClaimNames.Jti, userInfo.ログインID.ToString()),
                //(Issued At)
                new Claim(JwtRegisteredClaimNames.Iat, $"{new DateTimeOffset(DateTime.Now).ToUnixTimeSeconds()}"),
                //(Not Before)
                new Claim(JwtRegisteredClaimNames.Nbf, $"{new DateTimeOffset(DateTime.Now).ToUnixTimeSeconds()}") ,
                //JWT期限切れる時間
                //デフォルト時間は120分
                //(Expiration Time)
                new Claim(JwtRegisteredClaimNames.Exp, exp),
                //(Issuer)
                new Claim(JwtRegisteredClaimNames.Iss, AppSetting.Secret.Issuer),
                //(Audience)
                new Claim(JwtRegisteredClaimNames.Aud, AppSetting.Secret.Audience),
                //(Payload)
                new Claim(ClaimTypes.Role,userInfo.所属部署コード?.ToString()),
                new Claim(ClaimTypes.Name,userInfo.社員名.ToString()),
            };

            //暗証Key 16桁
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(AppSetting.Secret.JWT));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
            JwtSecurityToken securityToken = new JwtSecurityToken(issuer: AppSetting.Secret.Issuer, claims: claims, signingCredentials: creds);
            string jwt = new JwtSecurityTokenHandler().WriteToken(securityToken);
            return jwt;
        }
        /// <summary>
        /// 復号化
        /// </summary>
        /// <param name="jwtStr"></param>
        /// <returns></returns>
        public static UserInfo SerializeJwt(string jwtStr)
        {
            var jwtHandler = new JwtSecurityTokenHandler();
            JwtSecurityToken jwtToken = jwtHandler.ReadJwtToken(jwtStr);
            UserInfo userInfo = new UserInfo
            {
                ログインID = jwtToken.Id,
                所属部署コード = jwtToken.Payload[ClaimTypes.Role]?.ToString(),
                社員名 = jwtToken.Payload[ClaimTypes.Name]?.ToString()
            };
            return userInfo;
        }
        /// <summary>
        /// 期限を取得
        /// </summary>
        /// <param name="jwtStr"></param>
        /// <returns></returns>
        public static DateTime GetExp(string jwtStr)
        {
            var jwtHandler = new JwtSecurityTokenHandler();
            JwtSecurityToken jwtToken = jwtHandler.ReadJwtToken(jwtStr);

            DateTime expDate = (jwtToken.Payload[JwtRegisteredClaimNames.Exp] ?? 0).GetInt().GetTimeStampToDate();
            return expDate;
        }
        public static bool IsExp(string jwtStr)
        {
            return GetExp(jwtStr) < DateTime.Now;
        }
        public static string GetUserId(string jwtStr)
        {
            try
            {
                string token = jwtStr.Split(" ")[1];
                return new JwtSecurityTokenHandler().ReadJwtToken(token).Id;
            }
            catch
            {
                return string.Empty;
            }
        }
    }
}
