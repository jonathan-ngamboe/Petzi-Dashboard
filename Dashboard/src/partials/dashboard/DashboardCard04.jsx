import React from 'react';

function DashboardCard04({ customerInfo }) {
  // Calcul des totaux
  let totalSpent = 0;
  let totalPurchases = 0;

  Object.values(customerInfo).forEach(customerData => {
    totalSpent += customerData.totalSpending;
    totalPurchases += customerData.purchaseCount;
  });

  // Calcul du panier moyen global
  const averageBasketGlobal = totalPurchases ? (totalSpent / totalPurchases).toFixed(2) : 0;

  return (
    <div className="col-span-full xl:col-span-12 bg-white dark:bg-slate-800 shadow-lg rounded-sm border border-slate-200 dark:border-slate-700">
      <header className="px-5 py-4 border-b border-slate-100 dark:border-slate-700">
        <h2 className="font-semibold text-slate-800 dark:text-slate-100">Clients</h2>
      </header>
      <div className="p-3">
        {/* Table */}
        <div className="overflow-x-auto">
          <table className="table-auto w-full">
            {/* Table header */}
            <thead className="text-xs font-semibold uppercase text-slate-400 dark:text-slate-500 bg-slate-50 dark:bg-slate-700 dark:bg-opacity-50">
              <tr>
                <th className="p-2">
                  <div className="font-semibold text-left">Nom</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-left">Dépensé</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-center">Nombre d'achats</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-center">Panier Moyen</div>
                </th>
                <th className="p-2">
                  <div className="font-semibold text-center">Code Postal</div>
                </th>
              </tr>
            </thead>
            {/* Table body */}
            <tbody className="text-sm divide-y divide-slate-100 dark:divide-slate-700">
              {Object.entries(customerInfo).map(([customerName, customerData], index) => {
                // Calcul du panier moyen pour chaque client
                const averageBasket = customerData.purchaseCount
                  ? (customerData.totalSpending / customerData.purchaseCount).toFixed(2)
                  : 0;

                return (
                  <tr key={index}>
                    <td className="p-2">
                      <div className="text-slate-800 dark:text-slate-100">{customerName}</div>
                    </td>
                    <td className="p-2">
                      <div className="text-left text-green-500">CHF {customerData.totalSpending.toFixed(2)}</div>
                    </td>
                    <td className="p-2">
                      <div className="text-center">{customerData.purchaseCount}</div>
                    </td>
                    <td className="p-2">
                      <div className="text-center">CHF {averageBasket}</div>
                    </td>
                    <td className="p-2">
                      <div className="text-center">{customerData.postcode}</div>
                    </td>
                  </tr>
                );
              })}
              {/* Ligne des totaux */}
              <tr className="font-bold">
                <td className="p-2">Total</td>
                <td className="p-2">CHF {totalSpent.toFixed(2)}</td>
                <td className="p-2 text-center">{totalPurchases}</td>
                <td className="p-2 text-center">CHF {averageBasketGlobal}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default DashboardCard04;
