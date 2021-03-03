import React from 'react';
import {render} from '@testing-library/react';
import CodeAnalysis from '../../components/CodeAnalysis';

describe("Code Analysis", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');

    beforeEach(()=>{
        useRouter.mockClear();
        mockUseEffect.mockClear();
        mockAxios.mockClear();
    })

    it("Snapshot serverId", () => {
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        const { container } = render(
            <CodeAnalysis />
        )
        expect(container).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        render(<CodeAnalysis />);
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
            isReady: true,
        }));
        render(<CodeAnalysis />);
        expect(mockAxios).toHaveBeenCalledTimes(5);

        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
            isReady: false,
        }));
        render(<CodeAnalysis />);
        expect(mockAxios).toHaveBeenCalledTimes(5);
    })


})