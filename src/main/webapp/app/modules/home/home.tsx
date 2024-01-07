import { Button } from 'primereact/button';
import React from 'react';
import './home.scss';

import { AUTHORITIES } from 'app/config/constants';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { useNavigate } from 'react-router-dom';
import { translate } from 'react-jhipster';

const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const isTeacher = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.TEACHER]));
  const navigate = useNavigate();

  return (
    <div className="surface-0 flex justify-content-center">
      <div id="home" className="landing-wrapper overflow-hidden">
        <div
          id="hero"
          className="flex flex-column  px-4 lg:px-1 overflow-hidden"
          style={{
            background:
              'linear-gradient(0deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.2)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, #EEEFAF 0%, #C3E3FA 100%)',
            clipPath: 'ellipse(150% 87% at 93% 13%)',
          }}
        >
          <div className="mx-4 md:mx-8 mt-0 md:mt-2">
            <h1 className="text-6xl font-bold text-gray-900 line-height-2">
              <span className="font-light block">{translate('home.titleText1')}</span>
              {translate('home.titleText2')}
            </h1>
            <p className="font-normal text-2xl line-height-3 md:mt-3 text-gray-700">{translate('home.titleText3')} </p>
            <Button
              type="button"
              label={translate('home.getStarted')}
              className="p-button-rounded text-xl border-none mt-3 bg-blue-500 font-normal line-height-3 px-3 text-white"
            ></Button>
          </div>
          <div className="flex justify-content-center md:justify-content-end">
            <img src="/content/images/img_group.png" alt="Hero Image" className="w-9 md:w-auto" />
          </div>
        </div>

        <div id="features" className="py-4 px-4 lg:px-1 mt-5 mx-0 lg:mx-8">
          <div className="grid justify-content-center">
            <div className="col-12 text-center mt-8 mb-4">
              <h2 className="text-900 font-normal mb-2">{translate('home.marvelousFeatures')}</h2>
              <span className="text-600 text-2xl">Placerat in egestas erat...</span>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-yellow-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-users text-2xl text-yellow-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.easyToUse')}</h5>
                  <span className="text-600">Posuere morbi leo urna molestie.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(145,226,237,0.2),rgba(251, 199, 145, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(172, 180, 223, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-cyan-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-palette text-2xl text-cyan-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.freshDesign')}</h5>
                  <span className="text-600">Semper risus in hendrerit.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(145, 226, 237, 0.2), rgba(172, 180, 223, 0.2)), linear-gradient(180deg, rgba(172, 180, 223, 0.2), rgba(246, 158, 188, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-indigo-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-map text-2xl text-indigo-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.wellDocumented')}</h5>
                  <span className="text-600">Non arcu risus quis varius quam quisque.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(187, 199, 205, 0.2),rgba(251, 199, 145, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2),rgba(145, 210, 204, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-bluegray-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-id-card text-2xl text-bluegray-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.responsiveLayout')}</h5>
                  <span className="text-600">Nulla malesuada pellentesque elit.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(187, 199, 205, 0.2),rgba(246, 158, 188, 0.2)), linear-gradient(180deg, rgba(145, 226, 237, 0.2),rgba(160, 210, 250, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-orange-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-star text-2xl text-orange-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.cleanCode')}</h5>
                  <span className="text-600">Condimentum lacinia quis vel eros.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pb-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(251, 199, 145, 0.2), rgba(246, 158, 188, 0.2)), linear-gradient(180deg, rgba(172, 180, 223, 0.2), rgba(212, 162, 221, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-pink-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-moon text-2xl text-pink-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.darkMode')}</h5>
                  <span className="text-600">Convallis tellus id interdum velit laoreet.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(145, 210, 204, 0.2), rgba(160, 210, 250, 0.2)), linear-gradient(180deg, rgba(187, 199, 205, 0.2), rgba(145, 210, 204, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-teal-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-shopping-cart text-2xl text-teal-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.readyToUse')}</h5>
                  <span className="text-600">Mauris sit amet massa vitae.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg:pr-5 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(145, 210, 204, 0.2), rgba(212, 162, 221, 0.2)), linear-gradient(180deg, rgba(251, 199, 145, 0.2), rgba(160, 210, 250, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-blue-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-globe text-2xl text-blue-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.modernPractices')}</h5>
                  <span className="text-600">Elementum nibh tellus molestie nunc non.</span>
                </div>
              </div>
            </div>

            <div className="col-12 md:col-12 lg:col-4 p-0 lg-4 mt-4 lg:mt-0">
              <div
                style={{
                  height: '160px',
                  padding: '2px',
                  borderRadius: '10px',
                  background:
                    'linear-gradient(90deg, rgba(160, 210, 250, 0.2), rgba(212, 162, 221, 0.2)), linear-gradient(180deg, rgba(246, 158, 188, 0.2), rgba(212, 162, 221, 0.2))',
                }}
              >
                <div className="p-3 surface-card h-full" style={{ borderRadius: '8px' }}>
                  <div
                    className="flex align-items-center justify-content-center bg-purple-200 mb-3"
                    style={{ width: '3.5rem', height: '3.5rem', borderRadius: '10px' }}
                  >
                    <i className="pi pi-fw pi-eye text-2xl text-purple-700"></i>
                  </div>
                  <h5 className="mb-2 text-900">{translate('home.privacy')}</h5>
                  <span className="text-600">Neque egestas congue quisque.</span>
                </div>
              </div>
            </div>

            <div
              className="col-12 mt-8 mb-8 p-2 md:p-8"
              style={{
                borderRadius: '20px',
                background:
                  'linear-gradient(0deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.6)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, #EFE1AF 0%, #C3DCFA 100%)',
              }}
            >
              <div className="flex flex-column justify-content-center align-items-center text-center px-3 py-3 md:py-0">
                <h3 className="text-gray-900 mb-2">Joséphine Miller</h3>
                <span className="text-gray-600 text-2xl">Peak Interactive</span>
                <p className="text-gray-900 sm:line-height-2 md:line-height-4 text-2xl mt-4" style={{ maxWidth: '800px' }}>
                  “Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
                  occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.”
                </p>
                <img className="logo mr-2" src="/content/images/open-book-black.svg" />
              </div>
            </div>
          </div>
        </div>

        <div id="highlights" className="py-4 px-4 lg:px-1 mx-0 my-6 lg:mx-8">
          <div className="text-center">
            <h2 className="text-900 font-normal mb-2">{translate('home.powerfulEverywhere')}</h2>
            <span className="text-600 text-2xl">Amet consectetur adipiscing elit...</span>
          </div>

          <div className="grid mt-8 pb-2 md:pb-8">
            <div
              className="flex justify-content-center col-12 lg:col-6 bg-purple-100 p-0 flex-order-1 lg:flex-order-0"
              style={{ borderRadius: '8px' }}
            >
              <img src={`/content/images/img_mobile.jpg`} className="w-9" alt="mockup mobile" />
            </div>

            <div className="col-12 lg:col-6 my-auto flex flex-column lg:align-items-end text-center lg:text-right">
              <div
                className="flex align-items-center justify-content-center bg-purple-200 align-self-center lg:align-self-end"
                style={{ width: '4.2rem', height: '4.2rem', borderRadius: '10px' }}
              >
                <i className="pi pi-fw pi-mobile text-5xl text-purple-700"></i>
              </div>
              <h2 className="line-height-1 text-900 text-4xl font-normal">Congue Quisque Egestas</h2>
              <span className="text-700 text-2xl line-height-3 ml-0 md:ml-2" style={{ maxWidth: '650px' }}>
                Lectus arcu bibendum at varius vel pharetra vel turpis nunc. Eget aliquet nibh praesent tristique magna sit amet purus
                gravida. Sit amet mattis vulputate enim nulla aliquet.
              </span>
            </div>
          </div>

          <div className="grid my-8 pt-2 md:pt-8">
            <div className="col-12 lg:col-6 my-auto flex flex-column text-center lg:text-left lg:align-items-start">
              <div
                className="flex align-items-center justify-content-center bg-yellow-200 align-self-center lg:align-self-start"
                style={{ width: '4.2rem', height: '4.2rem', borderRadius: '10px' }}
              >
                <i className="pi pi-fw pi-desktop text-5xl text-yellow-700"></i>
              </div>
              <h2 className="line-height-1 text-900 text-4xl font-normal">Celerisque Eu Ultrices</h2>
              <span className="text-700 text-2xl line-height-3 mr-0 md:mr-2" style={{ maxWidth: '650px' }}>
                Adipiscing commodo elit at imperdiet dui. Viverra nibh cras pulvinar mattis nunc sed blandit libero. Suspendisse in est ante
                in. Mauris pharetra et ultrices neque ornare aenean euismod elementum nisi.
              </span>
            </div>

            <div
              className="flex justify-content-center flex-order-1 sm:flex-order-2 col-12 lg:col-6 bg-yellow-100 p-0"
              style={{ borderRadius: '8px' }}
            >
              <img src={`/content/images/img_desktop.jpg`} className="w-9" alt="mockup" />
            </div>
          </div>
        </div>

        <div className="py-4 px-4 mx-0 mt-8 lg:mx-8">
          <div className="grid justify-content-between">
            <div className="col-12 md:col-2" style={{ marginTop: '-1.5rem' }}>
              <div className="flex flex-wrap align-items-center justify-content-center md:justify-content-start md:mb-0 mb-3 cursor-pointer">
                <img className="logo mr-2" src="/content/images/open-book-black.svg" />

                <span className="font-medium text-3xl text-900">Eduvi</span>
              </div>
            </div>

            <div className="col-12 md:col-10 lg:col-7">
              <div className="grid text-center md:text-left">
                <div className="col-12 md:col-3">
                  <h4 className="font-medium text-2xl line-height-3 mb-3 text-900">{translate('home.company')}</h4>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.aboutUs')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.news')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.investorRelations')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.careers')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer text-700">{translate('home.mediaKit')}</div>
                </div>

                <div className="col-12 md:col-3 mt-4 md:mt-0">
                  <h4 className="font-medium text-2xl line-height-3 mb-3 text-900">{translate('home.resources')}</h4>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.getStarted')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.learn')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer text-700">{translate('home.caseStudies')}</div>
                </div>

                <div className="col-12 md:col-3 mt-4 md:mt-0">
                  <h4 className="font-medium text-2xl line-height-3 mb-3 text-900">{translate('home.community')}</h4>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.discord')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.events')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.faq')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer text-700">{translate('home.blog')}</div>
                </div>

                <div className="col-12 md:col-3 mt-4 md:mt-0">
                  <h4 className="font-medium text-2xl line-height-3 mb-3 text-900">{translate('home.legal')}</h4>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.brandPolicy')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer mb-2 text-700">{translate('home.privacyPolicy')}</div>
                  <div className="line-height-3 text-xl block cursor-pointer text-700">{translate('home.termsOfService')}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
