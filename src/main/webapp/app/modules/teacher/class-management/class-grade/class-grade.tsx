import React, { useEffect, useRef, useState } from 'react';
import './class-grade.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSortState } from 'react-jhipster';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { IUserCourse } from 'app/shared/model/user-course.model';
import { getAssimentGrades, getCourse, getUserById, getUserCourses } from './grade-util';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { IAssignmentGrade } from 'app/shared/model/assignment-grade.model';
import { IUser } from 'app/shared/model/user.model';
import { getEntities } from 'app/entities/user-course/user-course.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { Button } from 'primereact/button';
export interface GradeReview {
  studentId?: number | null;
  assignmentGrade?: IAssignmentGrade[];
  info?: IUser;
}

const ClassGrade = () => {
  const dt = useRef<DataTable<GradeReview[]>>(null);
  (window as any).a = dt;
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const { id } = useParams();
  const [data, setData] = useState<GradeReview[]>([]);
  const userCourseList: IUserCourse[] = useAppSelector(state => state.userCourse.entities);
  const course: ICourse = useAppSelector(state => state.course.entity);
  const loading = useAppSelector(state => state.userCourse.loading);
  const totalItems = useAppSelector(state => state.userCourse.totalItems);

  const getAllEntities = () => {
    dispatch(
      getUserCourses({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        query: id,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    dispatch(getCourse(id));
    console.log(dt.current?.getTable().rows);
  }, []);

  useEffect(() => {
    console.log('course', course);
  }, [course]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userGradesPromises = userCourseList.map(userCourse => getAssimentGrades(userCourse.userId));
        const userGradesResponses = await Promise.all(userGradesPromises);

        // Organize data
        const organizedData: GradeReview[] = await Promise.all(
          userGradesResponses.map(async (assignmentGradesResponse, index) => {
            let user;
            let assignmentGrades: IAssignmentGrade[];
            try {
              user = (await getUserById(userCourseList[index].userId)).data;

              // Extract data from Axios response
              assignmentGrades = assignmentGradesResponse.data;
              assignmentGrades = assignmentGrades.filter(assignmentGrade => {
                console.log(
                  'condition: ',
                  course.assignments.findIndex(assignment => assignment.id === assignmentGrade.assignment.id) !== -1,
                  assignmentGrade
                );
                course.assignments.findIndex(assignment => assignment.id === assignmentGrade.assignment.id) !== -1 &&
                  assignmentGrade.assignment !== null;
              });

              console.log(index, {
                studentId: user ? user.id : null,
                assignmentGrade: assignmentGrades,
                info: user,
              });
            } catch (err) {}
            return {
              studentId: user ? user.id : null,
              assignmentGrade: assignmentGrades,
              info: user,
            };
          })
        );

        setData(organizedData);
        return organizedData;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData().then(value => console.log(value));
    console.log('data:', data);
  }, [userCourseList]);

  // Rest of your component code remains unchanged

  const exportCSV = () => {
    const exportedData = data.map(row => {
      const rowData = {
        studentId: row.studentId,
      };

      course.assignments?.forEach((assignment, index) => {
        const assignmentGrade = row.assignmentGrade?.find(value => value.assignment?.id === assignment?.id);
        rowData[`assignment_${index + 1}_grade`] = assignmentGrade ? assignmentGrade.grade : null;
      });

      return rowData;
    });

    import('papaparse').then(papaparse => {
      const csv = papaparse.default.unparse(exportedData, { header: true });
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.setAttribute('download', 'grades.csv');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  };

  const exportXLSX = () => {
    import('xlsx').then(xlsx => {
      const exportedData = data.map(row => {
        const rowData = {
          'Student ID': row.studentId,
        };

        course.assignments?.forEach((assignment, index) => {
          const assignmentGrade = row.assignmentGrade?.find(value => value.assignment?.id === assignment?.id);
          rowData[`Assignment ${index + 1} Grade`] = assignmentGrade ? assignmentGrade.grade : null;
        });

        return rowData;
      });

      const ws = xlsx.utils.json_to_sheet(exportedData);
      const wb = xlsx.utils.book_new();
      xlsx.utils.book_append_sheet(wb, ws, 'Grades');
      xlsx.writeFile(wb, 'grades.xlsx');
    });
  };

  const header = (
    <div className="flex align-items-center justify-content-end gap-2">
      <Button type="button" icon="pi pi-file" rounded={true} onClick={exportCSV} data-pr-tooltip="CSV" />
      <Button type="button" icon="pi pi-file-excel" severity="success" rounded={true} onClick={exportXLSX} data-pr-tooltip="XLS" />
    </div>
  );

  return (
    <div className="class-detail-container">
      <DataTable ref={dt} stripedRows={true} value={data} tableStyle={{ minWidth: '50rem' }} header={header}>
        <Column key={'studentId'} field="studentId" header="Student ID"></Column>
        {/* TODO: implement final grade */}
        {course.assignments?.map((assignment, index) => (
          <Column
            key={'studentId'}
            header={assignment.name}
            field={assignment.name}
            body={(rowData: GradeReview) => {
              const assignmentGrade = rowData.assignmentGrade?.filter(value => value.assignment?.id === assignment?.id)?.[0];

              return assignmentGrade ? assignmentGrade.grade : null;
            }}
          ></Column>
        ))}
      </DataTable>
    </div>
  );
};

export default ClassGrade;
