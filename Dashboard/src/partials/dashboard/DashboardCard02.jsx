import React from 'react';
import BarChart from '../../charts/BarChart01';
import { tailwindConfig } from '../../utils/Utils';
import Tooltip from '../../components/Tooltip';

function DashboardCard02({ dailySales }) {
  // Fonction pour agréger les ventes par jour
const aggregateSalesByDate = (sales) => {
  const salesAggregatedByDate = {};

  // Accumuler les ventes pour chaque date
  Object.entries(sales).forEach(([date, value]) => {
    // Extraire la date sans l'heure
    const dateOnly = date.split(' ')[0];
    // Si la date existe déjà dans l'objet, additionner la valeur, sinon l'initialiser
    if (salesAggregatedByDate[dateOnly]) {
      salesAggregatedByDate[dateOnly] += value;
    } else {
      salesAggregatedByDate[dateOnly] = value;
    }
  });

  return salesAggregatedByDate;
};

const getLastThreeDaysSales = (sales) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const threeDaysAgo = new Date().setDate(today.getDate() - 3);
  const recentSales = Object.entries(sales).filter(([date]) =>
    new Date(date.split(' ')[0]) >= threeDaysAgo
  );

  const salesAggregatedByDate = recentSales.reduce((acc, [date, value]) => {
    const dateOnly = date.split(' ')[0];
    if (acc[dateOnly]) {
      acc[dateOnly] += value;
    } else {
      acc[dateOnly] = value;
    }
    return acc;
  }, {});

  return salesAggregatedByDate;
};

const dailySalesAggregated = getLastThreeDaysSales(dailySales);

  const chartData = {
    labels: Object.keys(dailySalesAggregated),
    datasets: [
      {
        label: 'Ventes',
        data: Object.values(dailySalesAggregated),
        backgroundColor: tailwindConfig().theme.colors.blue[400],
        hoverBackgroundColor: tailwindConfig().theme.colors.blue[500],
        barPercentage: 0.66,
        categoryPercentage: 0.66,
      },
    ],
  };  

  return (
    <div className="flex flex-col col-span-full sm:col-span-6 bg-white dark:bg-slate-800 shadow-lg rounded-sm border border-slate-200 dark:border-slate-700">
      <header className="px-5 py-4 border-b border-slate-100 dark:border-slate-700 flex items-center">
        <h2 className="font-semibold text-slate-800 dark:text-slate-100">Nombre de ventes totales</h2>
        <Tooltip className="ml-2">
          <div className="text-xs text-center whitespace-nowrap">Ventes totales réalisées sur toute la période</div>
        </Tooltip>
      </header>
      <BarChart data={chartData} width={595} height={248} />
    </div>
  );
}

export default DashboardCard02;