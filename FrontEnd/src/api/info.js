import { info } from '.';

export const getAptInfoByCode = aptCode => info.get(`/${aptCode}`);

export const getAptRankByCode = aptCode => info.get(`/rank/${aptCode}`);

export const getAptDealInfoByCode = aptCode => info.get(`/deal/${aptCode}`);

export const getChartInfoBySidoName = sidoName => info.get(`/chart/${sidoName}`);

export const plusViewCount = aptCode => info.get(`/view/${aptCode}`);

export const getAvgInGugun = () => info.get(`/deal/avg`);
