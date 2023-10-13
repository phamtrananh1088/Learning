export const menu = (options) => {
    return {
        data: [
            { MenuId: 'A', MenuName: '共通', SubMenuId: 'WD00000', SubMenuName: 'ログイン画面', Path: 'login', },
            { MenuId: 'A', MenuName: '共通', SubMenuId: 'WD00012', SubMenuName: 'お知らせ登録', Path: 'osiraseTouroku', },
            { MenuId: 'A', MenuName: '共通', SubMenuId: 'TD00002', SubMenuName: 'お知らせ表示', Path: 'oshiraseHyouji', },
            { MenuId: 'B', MenuName: '依頼', SubMenuId: 'WD00111', SubMenuName: '[修繕]依頼参照/承認', Path: 'syuzenIraiSansyoShonin', },
            { MenuId: 'B', MenuName: '依頼', SubMenuId: 'WD00120', SubMenuName: '依頼添付登録', Path: 'iraiTenpuTouroku', },
            { MenuId: 'B', MenuName: '依頼', SubMenuId: 'WD00130', SubMenuName: '帳票一覧', Path: 'tyohyoIchiran', },
            { MenuId: 'B', MenuName: '依頼', SubMenuId: 'WD00311', SubMenuName: '[定期]依頼参照/承認', Path: 'syuzenIraiSansyoShonin', },
            { MenuId: 'C', MenuName: '作業', SubMenuId: 'WD00420', SubMenuName: '作業状況月別一覧', Path: 'sagyoJoukyoTsukibetuIchiran', },
            { MenuId: 'D', MenuName: '承認', SubMenuId: 'WD00520', SubMenuName: '[修繕]依頼参照/承認', Path: 'syuzenIraiSansyoShonin', },
            { MenuId: 'D', MenuName: '承認', SubMenuId: 'WD00530', SubMenuName: '[定期]依頼参照/承認', Path: 'syuzenIraiSansyoShonin', },
            { MenuId: 'E', MenuName: 'マスタ管理', SubMenuId: 'WM00100', SubMenuName: 'カレンダマスタ設定', Path: 'calendarSettei', },
            { MenuId: 'F', MenuName: '一覧', SubMenuId: 'RD00802', SubMenuName: '未承認/承認履歴一覧', Path: 'rd00802', },
        ]
    }
}